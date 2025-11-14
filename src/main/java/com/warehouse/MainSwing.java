package com.warehouse;

import com.warehouse.ui.WarehouseSwingUI;
import javax.swing.SwingUtilities;

public class MainSwing {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            WarehouseSwingUI ui = new WarehouseSwingUI();
            ui.setVisible(true);
        });
    }
}
