package cn.com.trade365.sxca_proxy_exchange.core;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

/**
 * @author :lhl
 * @create :2018-12-03 18:11
 */
public class HeartBeat {
    private static final Logger log = LoggerFactory.getLogger(HeartBeat.class);

    public static void server() {
        try {
            ServerSocket serverSocket = new ServerSocket(8888);
            System.out.println("启动");
            while (true) {
                final Socket socket = serverSocket.accept();
                new Thread(() -> {
                    InputStream inputStream = null;
                    InputStreamReader inputStreamReader = null;
                    BufferedReader bufferedReader = null;
                    OutputStream outputStream = null;
                    PrintWriter printWriter = null;
                    try {
                        //根据输入输出流和客户端连接
                        //得到一个输入流，接收客户端传递的信息
                        inputStream = socket.getInputStream();
                        //提高效率，将自己字节流转为字符流
                        inputStreamReader = new InputStreamReader(inputStream);
                        //加入缓冲区
                        bufferedReader = new BufferedReader(inputStreamReader);
                        String temp;
                        StringBuilder info = new StringBuilder();
                        while ((temp = bufferedReader.readLine()) != null) {
                            info.append(temp + "\n");
                        }
                        log.info("服务端接收到心跳连接：" + info + ",当前客户端ip为：" + socket.getInetAddress().getHostAddress());
                        //获取一个输出流，向服务端发送信息
                        outputStream = socket.getOutputStream();
                        //将输出流包装成打印流
                        printWriter = new PrintWriter(outputStream);
                        JSONObject object = new JSONObject();
                        object.put("success", "200");
                        printWriter.print(object.toJSONString());
                        printWriter.flush();
                        socket.shutdownOutput();//关闭输出流
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    } finally {
                        //关闭相对应的资源
                        if (printWriter != null) {
                            printWriter.close();
                        }
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException e) {
                                ;
                            }
                        }
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException e) {
                                ;
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                ;
                            }
                        }
                        try {
                            socket.close();
                        } catch (IOException e) {
                            ;
                        }
                    }
                }, UUID.randomUUID().toString()).start();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        server();
    }
}
