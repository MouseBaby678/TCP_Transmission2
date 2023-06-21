import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("服务器启动，等待连接...");

            while (true) {
                Socket socket = serverSocket.accept();

                // 读取状态判断标头
                InputStream inputStream = socket.getInputStream();
                DataInputStream dataInputStream = new DataInputStream(inputStream);
                String operation = dataInputStream.readUTF();

                if (operation.equals("upload")) {
                    handleUpload(socket, dataInputStream);
                } else if (operation.equals("download")) {
                    handleDownload(socket, dataInputStream);
                } else if(operation.equals("test")){
                    System.out.println("测试连接成功");
                } else if(operation.equals("delete")){
                    handleDelete(socket, dataInputStream);
                }
                else {
                    System.out.println("未知操作：" + operation);
                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void handleUpload(Socket socket, DataInputStream dataInputStream) throws IOException {
        System.out.println("开始处理上传操作");

        // 接收文件名
        String fileName = dataInputStream.readUTF();
        System.out.println("上传文件：" + fileName);

        // 创建文件输出流
        FileOutputStream fileOutputStream = new FileOutputStream("E:\\TCP_Transmission2\\server_file\\"+fileName);

        // 接收文件内容
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = dataInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        }

        fileOutputStream.close();
        System.out.println("文件上传完成");
        socket.close();
    }

    private static void handleDownload(Socket socket, DataInputStream dataInputStream) throws IOException {
        System.out.println("开始处理下载操作");

        // 接收文件名
        String fileName = dataInputStream.readUTF();
        System.out.println("下载文件：" + fileName);


        FileInputStream fileInputStream = new FileInputStream("E:\\TCP_Transmission2\\server_file\\"+fileName);

        // 发送文件内容
        byte[] buffer = new byte[1024];
        int bytesRead;
        OutputStream outputStream = socket.getOutputStream();
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        fileInputStream.close();
        System.out.println("文件下载完成");
        socket.close();
    }


    private static void handleDelete(Socket socket, DataInputStream dataInputStream) throws IOException {
        System.out.println("开始处理删除操作");

        // 接收文件名
        String fileName = dataInputStream.readUTF();
        System.out.println("删除文件：" + fileName);

        String filePath = "E:\\TCP_Transmission2\\server_file\\" + fileName;
        File fileToDelete = new File(filePath);
        if (fileToDelete.delete()){
            System.out.println("文件删除完成");
        }
        socket.close();
    }
}
