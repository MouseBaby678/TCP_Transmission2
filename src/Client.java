import java.io.*;
import java.net.Socket;

public class Client {

    public static void testConnect(String IP, int PORT) throws IOException {
        Socket socket = new Socket(IP, PORT);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF("test");
        System.out.println("测试连接成功");
        socket.close();
    }

    public static void deleteFile(String IP, int PORT, String fileName) throws IOException {
        Socket socket = new Socket(IP, PORT);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF("delete");
        dataOutputStream.writeUTF(fileName);
        System.out.println("文件删除完成");
        socket.close();
    }

    public static void handleUpload(String IP, int PORT, String filePath) throws IOException {

        Socket socket = new Socket(IP, PORT);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF("upload");
        File file = new File(filePath);
        // 发送文件名
        dataOutputStream.writeUTF(file.getName());

        // 创建文件输入流
        FileInputStream fileInputStream = new FileInputStream(filePath);

        // 发送文件内容
        byte[] buffer = new byte[1024];
        int bytesRead;

        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        System.out.println("文件上传完成");
        socket.close();
    }

    public static void handleDownload(String IP, int PORT, String fileName) throws IOException {
        Socket socket = new Socket(IP, PORT);
        OutputStream outputStream = socket.getOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
        dataOutputStream.writeUTF("download");
        dataOutputStream.writeUTF(fileName);

        InputStream inputStream = socket.getInputStream();
        // 创建文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\TCP_Transmission2\\client_file\\" + fileName);

        // 接收文件内容
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }

        fileOutputStream.close();
        System.out.println("文件下载完成");
        socket.close();
    }
}
