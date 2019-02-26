/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.desktop.forms;

import java.awt.Cursor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import org.meteoinfo.global.MIMath;
import org.meteoinfo.table.DataRow;
import org.meteoinfo.layer.LayerTypes;
import org.meteoinfo.layer.MapLayer;
import org.meteoinfo.layer.VectorLayer;
import org.meteoinfo.shape.Shape;

/**
 *
 * @author yaqiang
 */
public class FrmSelectByAttributes extends javax.swing.JDialog {

    private final List<VectorLayer> _mapLayers;
    private VectorLayer _selectLayer;
    private String _selectField;
    private final FrmMain _frmMain;

    /**
     * Creates new form FrmSelectByAttributes
     * @param parent
     * @param modal
     */
    public FrmSelectByAttributes(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        _frmMain = (FrmMain) parent;
        //---- Add selectable layers
        _mapLayers = new ArrayList<>();
        int i;
        for (i = 0; i < _frmMain.getMapDocument().getActiveMapFrame().getMapView().getLayerNum(); i++) {
            MapLayer mLayer = _frmMain.getMapDocument().getActiveMapFrame().getMapView().getLayers().get(i);
            if (mLayer.getLayerType() == LayerTypes.VectorLayer) {
                VectorLayer aLayer = (VectorLayer) mLayer;
                _mapLayers.add(aLayer);
            }
        }

        this.jComboBox_Layer.removeAllItems();
        if (_mapLayers.size() > 0) {
            for (i = 0; i < _mapLayers.size(); i++) {
                this.jComboBox_Layer.addItem(_mapLayers.get(i).getLayerName());
            }
            this.jComboBox_Layer.setSelectedIndex(0);
        }

        //Add select methods
        this.jComboBox_Method.removeAllItems();
        this.jComboBox_Method.addItem("Create a new selection");
        this.jComboBox_Method.addItem("Add to current selection");
        this.jComboBox_Method.addItem("Remove from current selection");
        this.jComboBox_Method.addItem("Select from current selection");
        this.jComboBox_Method.setSelectedIndex(0);
    }

    private void get_Fields() {
        this.jList_Fields.removeAll();
        DefaultListModel listModel = new DefaultListModel();
        for (int i = 0; i < _selectLayer.getFieldNumber(); i++) {
            listModel.addElement(_selectLayer.getField(i).getColumnName());
        }
        this.jList_Fields.setModel(listModel);
        this.jList_Fields.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jList_Fields.setSelectedIndex(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jComboBox_Layer = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jComboBox_Method = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList_Fields = new javax.swing.JList();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList_UniqueValues = new javax.swing.JList();
        jButton_EQ = new javax.swing.JButton();
        jButton_NE = new javax.swing.JButton();
        jButton_And = new javax.swing.JButton();
        jButton_GT = new javax.swing.JButton();
        jButton_GE = new javax.swing.JButton();
        jButton_Or = new javax.swing.JButton();
        jButton_LT = new javax.swing.JButton();
        jButton_LE = new javax.swing.JButton();
        jButton_Not = new javax.swing.JButton();
        jButton_GetValues = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField_QueryString = new javax.swing.JTextField();
        jButton_Select = new javax.swing.JButton();
        jButton_Clear = new javax.swing.JButton();
        jCheckBox_OnlySelVisible = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Select By Attribute");

        jLabel1.setText("Layer:");

        jComboBox_Layer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_Layer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_LayerActionPerformed(evt);
            }
        });

        jLabel2.setText("Method:");

        jComboBox_Method.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel3.setText("Fields:");

        jList_Fields.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList_Fields.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_FieldsMouseClicked(evt);
            }
        });
        jList_Fields.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jList_FieldsValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jList_Fields);

        jLabel4.setText("Unique Values:");

        jList_UniqueValues.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jList_UniqueValuesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jList_UniqueValues);

        jButton_EQ.setText("=");
        jButton_EQ.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_EQActionPerformed(evt);
            }
        });

        jButton_NE.setText("<>");
        jButton_NE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_NEActionPerformed(evt);
            }
        });

        jButton_And.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton_And.setText("And");
        jButton_And.setPreferredSize(new java.awt.Dimension(53, 27));
        jButton_And.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_AndActionPerformed(evt);
            }
        });

        jButton_GT.setText(">");
        jButton_GT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GTActionPerformed(evt);
            }
        });

        jButton_GE.setText(">=");
        jButton_GE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GEActionPerformed(evt);
            }
        });

        jButton_Or.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton_Or.setText("Or");
        jButton_Or.setPreferredSize(new java.awt.Dimension(53, 27));
        jButton_Or.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_OrActionPerformed(evt);
            }
        });

        jButton_LT.setText("<");
        jButton_LT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LTActionPerformed(evt);
            }
        });

        jButton_LE.setText("<=");
        jButton_LE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_LEActionPerformed(evt);
            }
        });

        jButton_Not.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        jButton_Not.setText("Not");
        jButton_Not.setPreferredSize(new java.awt.Dimension(53, 27));
        jButton_Not.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_NotActionPerformed(evt);
            }
        });

        jButton_GetValues.setText("Get Values");
        jButton_GetValues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_GetValuesActionPerformed(evt);
            }
        });

        jLabel5.setText("Select query string:");

        jButton_Select.setText("Select");
        jButton_Select.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_SelectActionPerformed(evt);
            }
        });

        jButton_Clear.setText("Clear");
        jButton_Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton_ClearActionPerformed(evt);
            }
        });

        jCheckBox_OnlySelVisible.setText("Only selection visible");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(116, Short.MAX_VALUE)
                .addComponent(jButton_Select)
                .addGap(74, 74, 74)
                .addComponent(jButton_Clear)
                .addGap(108, 108, 108))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTextField_QueryString)
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jButton_EQ, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton_GT, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jButton_LT))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jButton_NE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton_GE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jButton_LE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jButton_Or, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton_Not, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton_And, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jButton_GetValues))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(13, 13, 13)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jComboBox_Layer, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox_Method, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addContainerGap())
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(21, 21, 21))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox_OnlySelVisible)
                            .addComponent(jLabel5))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox_Layer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox_Method, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1)
                            .addComponent(jScrollPane2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField_QueryString, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox_OnlySelVisible)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_Select)
                            .addComponent(jButton_Clear)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_EQ)
                            .addComponent(jButton_NE)
                            .addComponent(jButton_And, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_GT)
                            .addComponent(jButton_GE)
                            .addComponent(jButton_Or, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton_LT)
                            .addComponent(jButton_LE)
                            .addComponent(jButton_Not, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton_GetValues)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox_LayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_LayerActionPerformed
        // TODO add your handling code here:
        if (this.jComboBox_Layer.getItemCount() == 0) {
            return;
        }

        this._selectLayer = this._mapLayers.get(this.jComboBox_Layer.getSelectedIndex());
        this.get_Fields();
    }//GEN-LAST:event_jComboBox_LayerActionPerformed

    private void jList_FieldsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jList_FieldsValueChanged
        // TODO add your handling code here:
        if (this.jList_Fields.getSelectedValue() == null) {
            return;
        }

        String selField = this.jList_Fields.getSelectedValue().toString();
        if (!selField.equals(_selectField)) {
            this.jList_UniqueValues.removeAll();
            this.jList_UniqueValues.setEnabled(false);
            this.jButton_GetValues.setEnabled(true);
        }
        _selectField = selField;
    }//GEN-LAST:event_jList_FieldsValueChanged

    private void jButton_GetValuesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GetValuesActionPerformed
        // TODO add your handling code here:
        List<String> valueList = new ArrayList<String>();
        boolean isNumeric = MIMath.isNumeric(_selectLayer.getField(_selectField));

        for (int i = 0; i < _selectLayer.getAttributeTable().getTable().getRowCount(); i++) {
            if (!valueList.contains(_selectLayer.getAttributeTable().getTable().getValue(i, _selectField).toString())) {
                valueList.add(_selectLayer.getAttributeTable().getTable().getValue(i, _selectField).toString());
            }
        }
        Collections.sort(valueList);

        this.jList_UniqueValues.setEnabled(true);
        DefaultListModel listModel = new DefaultListModel();
        if (isNumeric) {
            for (String vStr : valueList) {
                listModel.addElement(vStr);
            }
        } else {
            for (String vStr : valueList) {
                listModel.addElement("'" + vStr + "'");
            }
        }

        this.jList_UniqueValues.setModel(listModel);
        this.jList_UniqueValues.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.jButton_GetValues.setEnabled(false);
    }//GEN-LAST:event_jButton_GetValuesActionPerformed

    private void jButton_EQActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_EQActionPerformed
        // TODO add your handling code here:
        this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + " = ");
    }//GEN-LAST:event_jButton_EQActionPerformed

    private void jButton_NEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_NEActionPerformed
        // TODO add your handling code here:
        this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + " <> ");
    }//GEN-LAST:event_jButton_NEActionPerformed

    private void jButton_GTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GTActionPerformed
        // TODO add your handling code here:
        this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + " > ");
    }//GEN-LAST:event_jButton_GTActionPerformed

    private void jButton_LTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LTActionPerformed
        // TODO add your handling code here:
        this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + " < ");
    }//GEN-LAST:event_jButton_LTActionPerformed

    private void jButton_GEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_GEActionPerformed
        // TODO add your handling code here:
        this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + " >= ");
    }//GEN-LAST:event_jButton_GEActionPerformed

    private void jButton_LEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_LEActionPerformed
        // TODO add your handling code here:
        this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + " <= ");
    }//GEN-LAST:event_jButton_LEActionPerformed

    private void jButton_AndActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_AndActionPerformed
        // TODO add your handling code here:
        this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + " and ");
    }//GEN-LAST:event_jButton_AndActionPerformed

    private void jButton_OrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_OrActionPerformed
        // TODO add your handling code here:
        this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + " or ");
    }//GEN-LAST:event_jButton_OrActionPerformed

    private void jButton_NotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_NotActionPerformed
        // TODO add your handling code here:
        this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + " not ");
    }//GEN-LAST:event_jButton_NotActionPerformed

    private void jList_FieldsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_FieldsMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + this.jList_Fields.getSelectedValue().toString());
        }
    }//GEN-LAST:event_jList_FieldsMouseClicked

    private void jList_UniqueValuesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jList_UniqueValuesMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            this.jTextField_QueryString.setText(this.jTextField_QueryString.getText() + this.jList_UniqueValues.getSelectedValue().toString());
        }
    }//GEN-LAST:event_jList_UniqueValuesMouseClicked

    private void jButton_SelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_SelectActionPerformed
        // TODO add your handling code here:
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        String expression = this.jTextField_QueryString.getText();
        boolean onlySelVisible = this.jCheckBox_OnlySelVisible.isSelected();
        List<DataRow> rows = this._selectLayer.getAttributeTable().getTable().select(expression);
        List<Integer> rowIdxs = new ArrayList<>();
        for (DataRow row : rows) {
            rowIdxs.add(row.getRowIndex());
        }

        int i;
        switch (this.jComboBox_Method.getSelectedIndex()) {
            case 0:    //Create a new selection
                for (i = 0; i < _selectLayer.getShapeNum(); i++) {
                    if (rowIdxs.contains(i)) {
                        _selectLayer.getShapes().get(i).setSelected(true);
                    } else {
                        _selectLayer.getShapes().get(i).setSelected(false);
                    }
                }
                break;
            case 1:    //Add to current selection
                for (i = 0; i < _selectLayer.getShapeNum(); i++) {
                    if (rowIdxs.contains(i)) {
                        _selectLayer.getShapes().get(i).setSelected(true);
                    }
                }
                break;
            case 2:    //Remove from current selection
                for (i = 0; i < _selectLayer.getShapeNum(); i++) {
                    if (rowIdxs.contains(i)) {
                        _selectLayer.getShapes().get(i).setSelected(false);
                    }
                }
                break;
            case 3:    //Select from current selection
                for (i = 0; i < _selectLayer.getShapeNum(); i++) {
                    if (_selectLayer.getShapes().get(i).isSelected()) {
                        if (!rowIdxs.contains(i)) {
                            _selectLayer.getShapes().get(i).setSelected(false);
                        }
                    }
                }
                break;
        }

        if (onlySelVisible) {
            for (Shape shape : _selectLayer.getShapes()) {
                shape.setVisible(shape.isSelected());
            }
        }

        this._frmMain.refreshMap();
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_jButton_SelectActionPerformed

    private void jButton_ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton_ClearActionPerformed
        // TODO add your handling code here:
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
        this._selectLayer.clearSelectedShapes();
        for (Shape shape : _selectLayer.getShapes()) {
            shape.setVisible(true);
        }
        this._frmMain.refreshMap();
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_jButton_ClearActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmSelectByAttributes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmSelectByAttributes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmSelectByAttributes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmSelectByAttributes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmSelectByAttributes dialog = new FrmSelectByAttributes(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton_And;
    private javax.swing.JButton jButton_Clear;
    private javax.swing.JButton jButton_EQ;
    private javax.swing.JButton jButton_GE;
    private javax.swing.JButton jButton_GT;
    private javax.swing.JButton jButton_GetValues;
    private javax.swing.JButton jButton_LE;
    private javax.swing.JButton jButton_LT;
    private javax.swing.JButton jButton_NE;
    private javax.swing.JButton jButton_Not;
    private javax.swing.JButton jButton_Or;
    private javax.swing.JButton jButton_Select;
    private javax.swing.JCheckBox jCheckBox_OnlySelVisible;
    private javax.swing.JComboBox jComboBox_Layer;
    private javax.swing.JComboBox jComboBox_Method;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JList jList_Fields;
    private javax.swing.JList jList_UniqueValues;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField_QueryString;
    // End of variables declaration//GEN-END:variables
}
