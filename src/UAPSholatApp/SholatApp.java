package UAPSholatApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

public class SholatApp extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    DefaultTableModel kotaModel, jadwalModel;
    JTable kotaTable, jadwalTable;

    ArrayList<String[]> kotaList = new ArrayList<>();

    final String FILE_KOTA = "kota.csv";
    final String FILE_JADWAL = "jadwal.csv";

    public SholatApp() {
        setTitle("Aplikasi Jadwal Sholat & Imsakiyah");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainPanel.add(menuPanel(), "menu");
        mainPanel.add(kotaPanel(), "kota");
        mainPanel.add(jadwalPanel(), "jadwal");

        add(mainPanel);
        cardLayout.show(mainPanel, "menu");
        setLocationRelativeTo(null);
    }


    JPanel menuPanel() {
        JPanel p = new JPanel(new GridLayout(3, 1, 10, 10));
        JButton b1 = new JButton("Manajemen Kota");
        JButton b2 = new JButton("Cek Jadwal Sholat");
        JButton b3 = new JButton("Keluar");

        b1.addActionListener(e -> {
            loadKota();
            cardLayout.show(mainPanel, "kota");
        });

        b2.addActionListener(e -> cardLayout.show(mainPanel, "jadwal"));
        b3.addActionListener(e -> System.exit(0));

        p.add(b1);
        p.add(b2);
        p.add(b3);
        return p;
    }


    JPanel kotaPanel() {
        JPanel p = new JPanel(new BorderLayout());

        kotaModel = new DefaultTableModel(new String[]{"ID", "Nama Kota"}, 0);
        kotaTable = new JTable(kotaModel);

        JPanel input = new JPanel();
        JTextField tfId = new JTextField(5);
        JTextField tfNama = new JTextField(10);

        JButton tambah = new JButton("Tambah");
        JButton hapus = new JButton("Hapus");
        JButton kembali = new JButton("Kembali");

        tambah.addActionListener(e -> {
            try {
                kotaModel.addRow(new String[]{tfId.getText(), tfNama.getText()});
                saveKota();
                tfId.setText("");
                tfNama.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Input tidak valid");
            }
        });

        hapus.addActionListener(e -> {
            int row = kotaTable.getSelectedRow();
            if (row >= 0) {
                kotaModel.removeRow(row);
                saveKota();
            }
        });

        kembali.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        input.add(new JLabel("ID"));
        input.add(tfId);
        input.add(new JLabel("Kota"));
        input.add(tfNama);
        input.add(tambah);
        input.add(hapus);
        input.add(kembali);

        p.add(new JScrollPane(kotaTable), BorderLayout.CENTER);
        p.add(input, BorderLayout.SOUTH);
        return p;
    }

    JPanel jadwalPanel() {
        JPanel p = new JPanel(new BorderLayout());

        jadwalModel = new DefaultTableModel(new String[]{"Kota", "Waktu", "Jam"}, 0);
        jadwalTable = new JTable(jadwalModel);

        JComboBox<String> cbKota = new JComboBox<>();
        JButton cek = new JButton("Cek");
        JButton kembali = new JButton("Kembali");

        loadKotaCombo(cbKota);

        cek.addActionListener(e -> loadJadwal(cbKota.getSelectedItem().toString()));
        kembali.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        JPanel top = new JPanel();
        top.add(new JLabel("Pilih Kota"));
        top.add(cbKota);
        top.add(cek);
        top.add(kembali);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(jadwalTable), BorderLayout.CENTER);
        return p;
    }

    // ================= FILE HANDLING =================
    void loadKota() {
        kotaModel.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_KOTA))) {
            String line;
            while ((line = br.readLine()) != null) {
                kotaModel.addRow(line.split(","));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "File kota tidak ditemukan");
        }
    }

    void saveKota() {
        try (PrintWriter pw = new PrintWriter(FILE_KOTA)) {
            for (int i = 0; i < kotaModel.getRowCount(); i++) {
                pw.println(
                        kotaModel.getValueAt(i, 0) + "," +
                                kotaModel.getValueAt(i, 1)
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan kota");
        }
    }

    void loadKotaCombo(JComboBox<String> cb) {
        cb.removeAllItems();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_KOTA))) {
            String line;
            while ((line = br.readLine()) != null) {
                cb.addItem(line.split(",")[1]);
            }
        } catch (Exception ignored) {}
    }

    void loadJadwal(String kota) {
        jadwalModel.setRowCount(0);
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_JADWAL))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(kota)) {
                    jadwalModel.addRow(data);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "File jadwal tidak ditemukan");
        }
    }

    public static void main(String[] args) {
        new SholatApp().setVisible(true);
    }
}