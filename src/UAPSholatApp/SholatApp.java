package UAPSholatApp;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class SholatApp extends JFrame {

    CardLayout cardLayout = new CardLayout();
    JPanel mainPanel = new JPanel(cardLayout);

    DefaultTableModel kotaModel, jadwalModel;
    JTable kotaTable, jadwalTable;

    JComboBox<String> cbKota = new JComboBox<>();
    JTextArea logArea = new JTextArea(6, 40);

    final String FILE_KOTA = "kota.csv";
    final String FILE_JADWAL = "jadwal.csv";

    public SholatApp() {
        setTitle("Aplikasi Jadwal Sholat - Manajemen Kota");
        setSize(850, 650);

        // --- 1. Window Event: Konfirmasi Keluar ---
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // Jangan langsung keluar
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                keluarAplikasi();
            }
        });

        mainPanel.add(menuPanel(), "menu");
        mainPanel.add(kotaPanel(), "kota");
        mainPanel.add(jadwalPanel(), "jadwal");

        logArea.setEditable(false);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBackground(new Color(240, 240, 240));
        JScrollPane logScroll = new JScrollPane(logArea);
        logScroll.setBorder(BorderFactory.createTitledBorder("Riwayat Aktivitas (Log)"));

        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
        add(logScroll, BorderLayout.SOUTH);

        cardLayout.show(mainPanel, "menu");
        setLocationRelativeTo(null);
    }

    private void writeLog(String pesan) {
        logArea.append("- " + pesan + "\n");
    }

    private void keluarAplikasi() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin keluar?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    JPanel menuPanel() {
        JPanel p = new JPanel(new GridLayout(3, 1, 15, 15));
        p.setBorder(BorderFactory.createEmptyBorder(100, 200, 100, 200));

        JButton b1 = new JButton("Manajemen Kota");
        JButton b2 = new JButton("Cek Jadwal Sholat");
        JButton b3 = new JButton("Keluar");

        b1.addActionListener(e -> {
            loadKota();
            cardLayout.show(mainPanel, "kota");
        });

        b2.addActionListener(e -> {
            loadKotaCombo();
            cardLayout.show(mainPanel, "jadwal");
        });

        // Memanggil Window Event closing secara manual
        b3.addActionListener(e -> keluarAplikasi());

        p.add(b1); p.add(b2); p.add(b3);
        return p;
    }

    JPanel kotaPanel() {
        JPanel p = new JPanel(new BorderLayout());
        kotaModel = new DefaultTableModel(new String[]{"ID", "Nama Kota"}, 0);
        kotaTable = new JTable(kotaModel);

        JPanel input = new JPanel(new FlowLayout());
        JTextField tfId = new JTextField(5);
        JTextField tfNama = new JTextField(15);

        JButton tambah = new JButton("Tambah");
        JButton edit = new JButton("Simpan Edit");
        JButton hapus = new JButton("Hapus");
        JButton kembali = new JButton("Kembali");

        // --- 2. Mouse Event: Klik Tabel Isi TextField ---
        kotaTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = kotaTable.getSelectedRow();
                if (row != -1) {
                    tfId.setText(kotaModel.getValueAt(row, 0).toString());
                    tfNama.setText(kotaModel.getValueAt(row, 1).toString());
                }
            }
        });

        // --- 3. Key Event: Enter untuk Tambah ---
        tfNama.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    tambah.doClick();
                }
            }
        });

        // --- 4. Key Event: Validasi ID Hanya Angka ---
        tfId.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (!Character.isDigit(e.getKeyChar())) {
                    e.consume(); // Abaikan karakter bukan angka
                }
            }
        });

        tambah.addActionListener(e -> {
            if (tfId.getText().isEmpty() || tfNama.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID dan Nama tidak boleh kosong!");
                return;
            }
            kotaModel.addRow(new String[]{tfId.getText(), tfNama.getText()});
            saveKota();
            writeLog("TAMBAH: " + tfNama.getText() + " (" + tfId.getText() + ")");
            tfId.setText(""); tfNama.setText("");
        });

        edit.addActionListener(e -> {
            int row = kotaTable.getSelectedRow();
            if (row != -1) {
                String oldName = kotaModel.getValueAt(row, 1).toString();
                kotaModel.setValueAt(tfId.getText(), row, 0);
                kotaModel.setValueAt(tfNama.getText(), row, 1);
                saveKota();
                writeLog("EDIT: " + oldName + " menjadi " + tfNama.getText());
                tfId.setText(""); tfNama.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Pilih baris di tabel dulu!");
            }
        });

        hapus.addActionListener(e -> {
            int row = kotaTable.getSelectedRow();
            if (row != -1) {
                String nama = kotaModel.getValueAt(row, 1).toString();
                kotaModel.removeRow(row);
                saveKota();
                writeLog("HAPUS: " + nama);
                tfId.setText(""); tfNama.setText("");
            }
        });

        kembali.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        input.add(new JLabel("ID (Angka):")); input.add(tfId);
        input.add(new JLabel("Kota:")); input.add(tfNama);
        input.add(tambah); input.add(edit); input.add(hapus); input.add(kembali);

        p.add(new JScrollPane(kotaTable), BorderLayout.CENTER);
        p.add(input, BorderLayout.SOUTH);
        return p;
    }

    JPanel jadwalPanel() {
        JPanel p = new JPanel(new BorderLayout());
        jadwalModel = new DefaultTableModel(new String[]{"Kota", "Waktu", "Jam"}, 0);
        jadwalTable = new JTable(jadwalModel);

        JButton cek = new JButton("Tampilkan Jadwal");
        JButton kembali = new JButton("Kembali");

        cek.addActionListener(e -> {
            if (cbKota.getSelectedItem() != null) {
                loadJadwal(cbKota.getSelectedItem().toString());
            }
        });

        kembali.addActionListener(e -> cardLayout.show(mainPanel, "menu"));

        JPanel top = new JPanel();
        top.add(new JLabel("Pilih Kota:"));
        top.add(cbKota);
        top.add(cek);
        top.add(kembali);

        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(jadwalTable), BorderLayout.CENTER);
        return p;
    }

    // ================= DATA HANDLING (FILE) =================

    void loadKota() {
        kotaModel.setRowCount(0);
        File f = new File(FILE_KOTA);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 2) kotaModel.addRow(d);
            }
        } catch (IOException e) { writeLog("Gagal baca file kota."); }
    }

    void saveKota() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_KOTA))) {
            for (int i = 0; i < kotaModel.getRowCount(); i++) {
                pw.println(kotaModel.getValueAt(i, 0) + "," + kotaModel.getValueAt(i, 1));
            }
        } catch (IOException e) { writeLog("Gagal simpan file kota."); }
    }

    void loadKotaCombo() {
        cbKota.removeAllItems();
        File f = new File(FILE_KOTA);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d.length >= 2) cbKota.addItem(d[1]);
            }
        } catch (IOException e) { }
    }

    void loadJadwal(String kota) {
        jadwalModel.setRowCount(0);
        File f = new File(FILE_JADWAL);
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "files jadwal.csv belum dibuat!");
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            boolean found = false;
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                if (d[0].equalsIgnoreCase(kota)) {
                    jadwalModel.addRow(d);
                    found = true;
                }
            }
            if (found) writeLog("Cek jadwal kota: " + kota);
            else writeLog("Jadwal kota " + kota + " tidak ditemukan di CSV.");
        } catch (IOException e) { }
    }

    public static void main(String[] args) {
        // Menjalankan di Event Dispatch Thread (Best Practice)
        SwingUtilities.invokeLater(() -> {
            new SholatApp().setVisible(true);
        });
    }
}