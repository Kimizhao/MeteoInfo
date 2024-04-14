"""
Contains a collection of thermodynamic calculations.

Ported from MetPy.
"""

import warnings
from .. import constants
from ..cbook import broadcast_indices
from .tools import find_bounding_indices, _less_or_close
from ..interpolate import interpolate_1d
import mipylib.numeric as np
import mipylib.numeric.optimize as so

__all__ = [
    'equivalent_potential_temperature','exner_function',
    'mixing_ratio','mixing_ratio_from_specific_humidity','potential_temperature',
    'relative_humidity_from_specific_humidity', 'relative_humidity_from_dewpoint',
    'saturation_mixing_ratio','saturation_vapor_pressure','temperature_from_potential_temperature',
    'virtual_temperature','dry_static_energy','isentropic_interpolation',
    'dewpoint','dewpoint_from_relative_humidity','specific_humidity_from_dewpoint',
    'specific_humidity_from_mixing_ratio','specific_humidity_from_relative_humidity'
    ]

def saturation_vapor_pressure(temperature):
    r"""Calculate the saturation water vapor (partial) pressure.

    Parameters
    ----------
    temperature : `float`
        The temperature (kelvin)

    Returns
    -------
    `float`
        The saturation water vapor (partial) pressure

    See Also
    --------
    vapor_pressure, dewpoint

    Notes
    -----
    Instead of temperature, dewpoint may be used in order to calculate
    the actual (ambient) water vapor (partial) pressure.
    The formula used is that from [Bolton1980]_ for T in degrees Celsius:

    .. math:: 6.112 e^\frac{17.67T}{T + 243.5}
    """
    # Converted from original in terms of C to use kelvin.
    return constants.sat_pressure_0c * np.exp(17.67 * (temperature - 273.15) /
                                                     (temperature - 29.65))

def mixing_ratio_from_specific_humidity(specific_humidity):
    r"""Calculate the mixing ratio from specific humidity.
    Parameters
    ----------
    specific_humidity: `pint.Quantity`
        Specific humidity of air
    Returns
    -------
    `pint.Quantity`
        Mixing ratio
    Notes
    -----
    Formula from [Salby1996]_ pg. 118.
    .. math:: w = \frac{q}{1-q}
    * :math:`w` is mixing ratio
    * :math:`q` is the specific humidity
    See Also
    --------
    mixing_ratio, specific_humidity_from_mixing_ratio
    """
    return specific_humidity / (1 - specific_humidity)

def mixing_ratio(part_press, tot_press):
    """
    Calculates the mixing ratio of gas given its partial pressure
    and the total pressure of the air.

    There are no required units for the input arrays, other than that
    they have the same units.

    Parameters
    ----------
    part_press : array_like
        Partial pressure of the constituent gas
    tot_press : array_like
        Total air pressure

    Returns
    -------
    array_like
        The (mass) mixing ratio, dimensionless (e.g. Kg/Kg or g/g)

    See Also
    --------
    vapor_pressure
    """

    return constants.epsilon * part_press / (tot_press - part_press)

def saturation_mixing_ratio(tot_press, temperature):
    """
    Calculates the saturation mixing ratio given total pressure
    and the temperature.

    The implementation uses the formula outlined in [4]

    Parameters
    ----------
    tot_press: array_like
        Total atmospheric pressure (hPa)

    temperature: array_like
        The temperature (kelvin)

    Returns
    -------
    array_like
        The saturation mixing ratio, dimensionless

    References
    ----------
    .. [4] Hobbs, Peter V. and Wallace, John M., 1977: Atmospheric Science, an Introductory
            Survey. 73.
    """

    return mixing_ratio(saturation_vapor_pressure(temperature), tot_press)

def relative_humidity_from_specific_humidity(pressure, temperature, specific_humidity):
    r"""Calculate the relative humidity from specific humidity, temperature, and pressure.

    Parameters
    ----------
    pressure: `array`
        Total atmospheric pressure
    temperature: `array`
        Air temperature
    specific_humidity: `array`
        Specific humidity of air

    Returns
    -------
    `array`
        Relative humidity

    Notes
    -----
    Formula based on that from [Hobbs1977]_ pg. 74. and [Salby1996]_ pg. 118.

    .. math:: RH = \frac{q}{(1-q)w_s}

    * :math:`RH` is relative humidity as a unitless ratio
    * :math:`q` is specific humidity
    * :math:`w_s` is the saturation mixing ratio

    See Also
    --------
    relative_humidity_from_mixing_ratio
    """
    return (mixing_ratio_from_specific_humidity(specific_humidity)
            / saturation_mixing_ratio(pressure, temperature))

def relative_humidity_from_dewpoint(temperature, dewpoint):
    r"""Calculate the relative humidity.

    Uses temperature and dewpoint to calculate relative humidity as the ratio of vapor
    pressure to saturation vapor pressures.

    Parameters
    ----------
    temperature : `float`
        Air temperature (celsius)

    dewpoint : `float`
        Dewpoint temperature (celsius)

    Returns
    -------
    `float`
        Relative humidity

    Examples
    --------
    >>> relative_humidity_from_dewpoint(25, 12)
    <44.2484765>

    See Also
    --------
    saturation_vapor_pressure

    Notes
    -----
    .. math:: RH = \frac{e(T_d)}{e_s(T)}

    """
    e = saturation_vapor_pressure(dewpoint + 273.15)
    e_s = saturation_vapor_pressure(temperature + 273.15)
    return e / e_s

def exner_function(pressure, reference_pressure=constants.P0):
    r"""Calculate the Exner function.

    .. math:: \Pi = \left( \frac{p}{p_0} \right)^\kappa

    This can be used to calculate potential temperature from temperature (and visa-versa),
    since

    .. math:: \Pi = \frac{T}{\theta}

    Parameters
    ----------
    pressure : `pint.Quantity`
        The total atmospheric pressure
    reference_pressure : `pint.Quantity`, optional
        The reference pressure against which to calculate the Exner function, defaults to P0

    Returns
    -------
    `pint.Quantity`
        The value of the Exner function at the given pressure

    See Also
    --------
    potential_temperature
    temperature_from_potential_temperature
    """
    return (pressure / reference_pressure)**constants.kappa

def potential_temperature(pressure, temperature):
    """
    Calculate the potential temperature.

    Uses the Poisson equation to calculation the potential temperature
    given `pressure` and `temperature`.

    Parameters
    ----------
    pressure : array_like
        The total atmospheric pressure
    temperature : array_like
        The temperature

    Returns
    -------
    array_like
        The potential temperature corresponding to the temperature and
        pressure.

    See Also
    --------
    dry_lapse

    Notes
    -----
    Formula:

    .. math:: \Theta = T (P_0 / P)^\kappa
    """
    return temperature / exner_function(pressure)

def temperature_from_potential_temperature(pressure, potential_temperature):
    r"""Calculate the temperature from a given potential temperature.

    Uses the inverse of the Poisson equation to calculate the temperature from a
    given potential temperature at a specific pressure level.

    Parameters
    ----------
    pressure : `array`
        The total atmospheric pressure (hPa)
    potential_temperature : `array`
        The potential temperature (Kelvin)

    Returns
    -------
    `array` (kelvin)
        The temperature corresponding to the potential temperature and pressure.

    See Also
    --------
    dry_lapse
    potential_temperature

    Notes
    -----
    Formula:

    .. math:: T = \Theta (P / P_0)^\kappa
    """
    return potential_temperature * exner_function(pressure)

def equivalent_potential_temperature(pressure, temperature, dewpoint):
    r"""Calculate equivalent potential temperature.

    This calculation must be given an air parcel's pressure, temperature, and dewpoint.
    The implementation uses the formula outlined in [Bolton1980]_:
    First, the LCL temperature is calculated:

    .. math:: T_{L}=\frac{1}{\frac{1}{T_{D}-56}+\frac{ln(T_{K}/T_{D})}{800}}+56
    Which is then used to calculate the potential temperature at the LCL:

    .. math:: \theta_{DL}=T_{K}\left(\frac{1000}{p-e}\right)^k
              \left(\frac{T_{K}}{T_{L}}\right)^{.28r}

    Both of these are used to calculate the final equivalent potential temperature:
    .. math:: \theta_{E}=\theta_{DL}\exp\left[\left(\frac{3036.}{T_{L}}
                                              -1.78\right)*r(1+.448r)\right]

    Parameters
    ----------
    pressure: `float`
        Total atmospheric pressure (hPa)
    temperature: `float`
        Temperature of parcel (kelvin)
    dewpoint: `float`
        Dewpoint of parcel (kelvin)

    Returns
    -------
    `float`
        The equivalent potential temperature of the parcel (kelvin)
    Notes
    -----
    [Bolton1980]_ formula for Theta-e is used, since according to
    [DaviesJones2009]_ it is the most accurate non-iterative formulation
    available.
    """
    t = temperature
    td = dewpoint
    e = saturation_vapor_pressure(dewpoint)
    r = saturation_mixing_ratio(pressure, dewpoint)

    t_l = 56 + 1. / (1. / (td - 56) + np.log(t / td) / 800.)
    th_l = potential_temperature(pressure - e, temperature) * (t / t_l) ** (0.28 * r)
    return th_l * np.exp(r * (1 + 0.448 * r) * (3036. / t_l - 1.78))

def virtual_temperature(temperature, mixing, molecular_weight_ratio=constants.epsilon):
    r"""Calculate virtual temperature.

    This calculation must be given an air parcel's temperature and mixing ratio.
    The implementation uses the formula outlined in [Hobbs2006]_ pg.80.

    Parameters
    ----------
    temperature: `array`
        air temperature
    mixing : `array`
        dimensionless mass mixing ratio
    molecular_weight_ratio : float, optional
        The ratio of the molecular weight of the constituent gas to that assumed
        for air. Defaults to the ratio for water vapor to dry air.
        (:math:`\epsilon\approx0.622`).

    Returns
    -------
    `array`
        The corresponding virtual temperature of the parcel

    Notes
    -----
    .. math:: T_v = T \frac{\text{w} + \epsilon}{\epsilon\,(1 + \text{w})}

    """
    return temperature * ((mixing + molecular_weight_ratio)
                          / (molecular_weight_ratio * (1 + mixing)))

def dry_static_energy(height, temperature):
    r"""Calculate the dry static energy of parcels.

    This function will calculate the dry static energy following the first two terms of
    equation 3.72 in [Hobbs2006]_.

    Parameters
    ----------
    height : `array`
        Atmospheric height
    temperature : `array`
        Air temperature

    Returns
    -------
    `array`
        Dry static energy

    See Also
    --------
    montgomery_streamfunction

    Notes
    -----
    .. math:: \text{dry static energy} = c_{pd} T + gz

    * :math:`T` is temperature
    * :math:`z` is height
    """
    return constants.g * height + constants.Cp_d * temperature

def isentropic_interpolation(levels, pressure, temperature, *args, **kwargs):
    r"""Interpolate data in isobaric coordinates to isentropic coordinates.

    Parameters
    ----------
    levels : array
        One-dimensional array of desired potential temperature surfaces
    pressure : array
        One-dimensional array of pressure levels
    temperature : array
        Array of temperature
    vertical_dim : int, optional
        The axis corresponding to the vertical in the temperature array, defaults to 0.
    temperature_out : bool, optional
        If true, will calculate temperature and output as the last item in the output list.
        Defaults to False.
    max_iters : int, optional
        Maximum number of iterations to use in calculation, defaults to 50.
    eps : float, optional
        The desired absolute error in the calculated value, defaults to 1e-6.
    bottom_up_search : bool, optional
        Controls whether to search for levels bottom-up, or top-down. Defaults to
        True, which is bottom-up search.
    args : array, optional
        Any additional variables will be interpolated to each isentropic level

    Returns
    -------
    list
        List with pressure at each isentropic level, followed by each additional
        argument interpolated to isentropic coordinates.

    See Also
    --------
    potential_temperature, isentropic_interpolation_as_dataset

    Notes
    -----
    Input variable arrays must have the same number of vertical levels as the pressure levels
    array. Pressure is calculated on isentropic surfaces by assuming that temperature varies
    linearly with the natural log of pressure. Linear interpolation is then used in the
    vertical to find the pressure at each isentropic level. Interpolation method from
    [Ziv1994]_. Any additional arguments are assumed to vary linearly with temperature and will
    be linearly interpolated to the new isentropic levels.
    Will only return Pint Quantities, even when given xarray DataArray profiles. To
    obtain a xarray Dataset instead, use `isentropic_interpolation_as_dataset` instead.
    """
    # iteration function to be used later
    # Calculates theta from linearly interpolated temperature and solves for pressure
    def _isen_iter(iter_log_p, isentlevs_nd, ka, a, b, pok):
        exner = pok * np.exp(-ka * iter_log_p)
        t = a * iter_log_p + b
        # Newton-Raphson iteration
        f = isentlevs_nd - t * exner
        fp = exner * (ka * t - a)
        return iter_log_p - (f / fp)

    # Get dimensions in temperature
    ndim = temperature.ndim

    # Convert units
    #pressure = pressure.to('hPa')
    #temperature = temperature.to('kelvin')
    vertical_dim = kwargs.pop('vertical_dim', 0)
    slices = [np.newaxis] * ndim
    slices[vertical_dim] = slice(None)
    slices = tuple(slices)
    pressure = np.broadcast_to(pressure[slices], temperature.shape)

    # Sort input data
    sort_pressure = np.argsort(pressure, axis=vertical_dim)
    sort_pressure = np.swapaxes(np.swapaxes(sort_pressure, 0, vertical_dim)[::-1], 0,
                                vertical_dim)
    sorter = broadcast_indices(pressure, sort_pressure, ndim, vertical_dim)
    levs = pressure[sorter]
    tmpk = temperature[sorter]

    levels = np.asarray(levels).reshape(-1)
    isentlevels = levels[np.argsort(levels)]

    # Make the desired isentropic levels the same shape as temperature
    shape = list(temperature.shape)
    shape[vertical_dim] = isentlevels.size
    isentlevs_nd = np.broadcast_to(isentlevels[slices], shape)

    # exponent to Poisson's Equation, which is imported above
    ka = constants.kappa

    # calculate theta for each point
    pres_theta = potential_temperature(levs, tmpk)

    # Raise error if input theta level is larger than pres_theta max
    if np.max(pres_theta) < np.max(levels):
        raise ValueError('Input theta level out of data bounds')

    # Find log of pressure to implement assumption of linear temperature dependence on
    # ln(p)
    log_p = np.log(levs)

    # Calculations for interpolation routine
    pok = constants.P0 ** ka

    # index values for each point for the pressure level nearest to the desired theta level
    bottom_up_search = kwargs.pop('bottom_up_search', True)
    above, below, good = find_bounding_indices(pres_theta, levels, vertical_dim,
                                               from_below=bottom_up_search)

    # calculate constants for the interpolation
    a = (tmpk[above] - tmpk[below]) / (log_p[above] - log_p[below])
    b = tmpk[above] - a * log_p[above]

    # calculate first guess for interpolation
    isentprs = 0.5 * (log_p[above] + log_p[below])

    # Make sure we ignore any nans in the data for solving; checking a is enough since it
    # combines log_p and tmpk.
    good &= ~np.isnan(a)

    # iterative interpolation using scipy.optimize.fixed_point and _isen_iter defined above
    max_iters = kwargs.pop('max_iters', 50)
    eps = kwargs.pop('eps', 1e-6)
    log_p_solved = so.fixed_point(_isen_iter, isentprs[good],
                                  args=(isentlevs_nd[good], ka, a[good], b[good], pok),
                                  xtol=eps, maxiter=max_iters)

    # get back pressure from log p
    isentprs[good] = np.exp(log_p_solved)

    # Mask out points we know are bad as well as points that are beyond the max pressure
    isentprs[~(good & _less_or_close(isentprs, np.max(pressure)))] = np.nan

    # create list for storing output data
    ret = [isentprs]

    # if temperature_out = true, calculate temperature and output as last item in list
    temperature_out = kwargs.pop('temperature_out', False)
    if temperature_out:
        ret.append(isentlevs_nd / ((constants.P0 / isentprs) ** ka))

    # do an interpolation for each additional argument
    if args:
        others = interpolate_1d(isentlevels, pres_theta, *(arr[sorter] for arr in args),
                                axis=vertical_dim, return_list_always=True)
        ret.extend(others)

    return ret

def dewpoint_from_relative_humidity(temperature, rh):
    r"""Calculate the ambient dewpoint given air temperature and relative humidity.
    Parameters
    ----------
    temperature : `float`
        Air temperature (celsius)
    rh : `float`
        Relative humidity expressed as a ratio in the range 0 < rh <= 1
    Returns
    -------
    `float`
        The dew point temperature (celsius)
    See Also
    --------
    dewpoint, saturation_vapor_pressure
    """
    if np.any(rh > 1.2):
        warnings.warn('Relative humidity >120%, ensure proper units.')
    return dewpoint(rh * saturation_vapor_pressure(temperature))

def dewpoint(vapor_pressure):
    r"""Calculate the ambient dewpoint given the vapor pressure.

    Parameters
    ----------
    vapor_pressure : `array`
        Water vapor partial pressure

    Returns
    -------
    `array`
        Dew point temperature

    See Also
    --------
    dewpoint_rh, saturation_vapor_pressure, vapor_pressure

    Notes
    -----
    This function inverts the [Bolton1980]_ formula for saturation vapor
    pressure to instead calculate the temperature. This yield the following
    formula for dewpoint in degrees Celsius:

    .. math:: T = \frac{243.5 log(e / 6.112)}{17.67 - log(e / 6.112)}
    """
    val = np.log(vapor_pressure / constants.nounit.sat_pressure_0c)
    return constants.nounit.zero_degc + 243.5 * val / (17.67 - val)

def specific_humidity_from_mixing_ratio(mixing_ratio):
    r"""Calculate the specific humidity from the mixing ratio.

    Parameters
    ----------
    mixing_ratio: `array`
        Mixing ratio

    Returns
    -------
    `array`
        Specific humidity

    See Also
    --------
    mixing_ratio, mixing_ratio_from_specific_humidity

    Notes
    -----
    Formula from [Salby1996]_ pg. 118.

    .. math:: q = \frac{w}{1+w}

    * :math:`w` is mixing ratio

    * :math:`q` is the specific humidity
    """
    return mixing_ratio / (1 + mixing_ratio)

def specific_humidity_from_dewpoint(pressure, dewpoint):
    r"""Calculate the specific humidity from the dewpoint temperature and pressure.

    Parameters
    ----------
    dewpoint: `array`
        Dewpoint temperature
    pressure: `array`
        Pressure

    Returns
    -------
    `array`
        Specific humidity

    See Also
    --------
    mixing_ratio, saturation_mixing_ratio
    """
    mixing_ratio = saturation_mixing_ratio(pressure, dewpoint)
    return specific_humidity_from_mixing_ratio(mixing_ratio)

def specific_humidity_from_relative_humidity(pressure, temperature, rh):
    """Calculate specific humidity from relative humidity, pressure and temperature.

    Parameters
    ----------
    pressure: `array`
        Pressure
    temperature: `array`
        temperature
    rh: `array`
        relative humidity

    Returns
    -------
    `array`
        Specific humidity
    """
    dp = dewpoint_from_relative_humidity(temperature, rh)
    return specific_humidity_from_dewpoint(pressure, dp)