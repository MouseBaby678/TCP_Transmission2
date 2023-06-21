
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class ClientFrame extends JFrame {

    private JPanel contentPane;
    private JTextField ipTextField;
    private JTextField portTextField;
    private JTextField fileTextField;
    private JTable serverFileTable;
    private static Client client;
    private static Server server;
    private final DefaultTableModel model;

    private void refreshServerFileTable(DefaultTableModel model) {
        model.setRowCount(0); // 清空表格内容
        File directory = new File("E:\\TCP_Transmission2\\server_file\\");
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String name = file.getName();
                long size = file.length();
                model.addRow(new Object[]{name, size});
            }
        }
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) throws IOException {
        try {
            ClientFrame frame = new ClientFrame();
            frame.setVisible(true);
            frame.refreshServerFileTable(frame.model);
            server = new Server();
            client = new Client();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the frame.
     */
    public ClientFrame() {
        setTitle("Client");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500, 250, 600, 400);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("服务器IP地址：");
        lblNewLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        lblNewLabel.setBounds(56, 34, 124, 38);
        contentPane.add(lblNewLabel);

        ipTextField = new JTextField();
        ipTextField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        ipTextField.setBounds(176, 46, 106, 21);
        contentPane.add(ipTextField);
        ipTextField.setColumns(10);

        JLabel portLabel = new JLabel("端口：");
        portLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        portLabel.setBounds(298, 34, 59, 38);
        contentPane.add(portLabel);

        portTextField = new JTextField();
        portTextField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        portTextField.setColumns(10);
        portTextField.setBounds(352, 46, 78, 21);
        contentPane.add(portTextField);

        JButton connectButton = new JButton("测试连接");
        connectButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        connectButton.setBounds(446, 43, 108, 29);
        contentPane.add(connectButton);
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client.testConnect(ipTextField.getText(), Integer.parseInt(portTextField.getText()));
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JLabel lblNewLabel_2 = new JLabel("上传文件：");
        lblNewLabel_2.setFont(new Font("微软雅黑", Font.BOLD, 18));
        lblNewLabel_2.setBounds(56, 107, 89, 38);
        contentPane.add(lblNewLabel_2);

        fileTextField = new JTextField();
        fileTextField.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        fileTextField.setColumns(10);
        fileTextField.setBounds(140, 116, 220, 25);
        contentPane.add(fileTextField);

        JButton selectButton = new JButton("选择文件");
        selectButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        selectButton.setBounds(370, 113, 108, 29);
        contentPane.add(selectButton);

        JButton uploadButton = new JButton("上传");
        uploadButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        uploadButton.setBounds(484, 113, 72, 29);
        contentPane.add(uploadButton);

        JLabel serverFileLabel = new JLabel("服务器文件：");
        serverFileLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        serverFileLabel.setBounds(56, 155, 112, 38);
        contentPane.add(serverFileLabel);

        JScrollPane serverFileScrollPane = new JScrollPane();
        serverFileScrollPane.setBounds(176, 169, 300, 147);
        contentPane.add(serverFileScrollPane);

        serverFileTable = new JTable();
        serverFileTable.setFont(new Font("微软雅黑", Font.BOLD, 14));
        model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"文件名称", "文件大小"}
        );
        serverFileTable.setModel(model);
        serverFileTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));
        serverFileTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        serverFileTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        serverFileScrollPane.setViewportView(serverFileTable);


        JButton deleteButton = new JButton("删除");
        deleteButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        deleteButton.setBounds(484, 184, 72, 29);
        contentPane.add(deleteButton);

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = serverFileTable.getSelectedRow();
                if (selectedRow != -1) {
                    String fileName = (String) model.getValueAt(selectedRow, 0);
                    try {
                        Client.deleteFile(ipTextField.getText(), Integer.parseInt(portTextField.getText()), fileName);
                        Thread.sleep(500);
                        refreshServerFileTable(model);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (InterruptedException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(contentPane, "请先选择要删除的文件！");
                }
            }
        });

        JButton refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        refreshButton.setBounds(484, 225, 72, 29);
        contentPane.add(refreshButton);

        refreshButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                refreshServerFileTable(model);
            }
        });


        JButton downloadButton = new JButton("下载");
        downloadButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        downloadButton.setBounds(484, 268, 72, 29);
        contentPane.add(downloadButton);
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("./");
                fileChooser.setMultiSelectionEnabled(true);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int option = fileChooser.showOpenDialog(ClientFrame.this);
                if (option == JFileChooser.APPROVE_OPTION) {
                    String path = fileChooser.getSelectedFile().getPath();
                    fileTextField.setText(path);
                } else {
                    fileTextField.setText("");
                }
            }
        });

        uploadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Client.handleUpload(ipTextField.getText(), Integer.parseInt(portTextField.getText()),fileTextField.getText());
                    Thread.sleep(500);
                    refreshServerFileTable(model);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        downloadButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                int selectedRow = serverFileTable.getSelectedRow();
                if (selectedRow != -1) {
                    String fileName = (String) serverFileTable.getValueAt(selectedRow, 0);

                    try {
                        Client.handleDownload(ipTextField.getText(), Integer.parseInt(portTextField.getText()),fileName);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请先选择要下载的文件！", "提示", JOptionPane.WARNING_MESSAGE);
                }

            }
        });
        ipTextField.setText("127.0.0.1");
        portTextField.setText("8888");
    }
}
