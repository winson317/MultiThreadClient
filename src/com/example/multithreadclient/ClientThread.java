package com.example.multithreadclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class ClientThread implements Runnable {
	
	private Socket socket;
	private Handler handler; //������UI�̷߳�����Ϣ��Handler����
	public Handler revHandler; //�������UI�̵߳���Ϣ��Handler����
	BufferedReader bufferedReader = null;  //���߳��������Socket����Ӧ��������
	OutputStream outputStream = null;
	
	public ClientThread(Handler handler)
	{
		this.handler = handler;
	}

	@Override
	public void run() 
	{
		try 
		{
			socket = new Socket("192.168.1.88", 30000);
			bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			outputStream = socket.getOutputStream();
			//����һ�����߳�����ȡ��������Ӧ������
			new Thread()
			{
				@Override
				public void run() 
				{
					String content = null;
					//���϶�ȡsocket�������е�����
					try 
					{
						while ((content = bufferedReader.readLine()) != null) 
						{
							Message msg = new Message(); //ÿ���������Է�����������֮�󣬷�����Ϣ֪ͨ�߳̽�����ʾ������
							msg.what = 0x123;
							msg.obj = content;
							handler.sendMessage(msg);
						}
					} 
					catch (Exception e) {
						e.printStackTrace();
					}
					
				}

			}.start();
			
			Looper.prepare(); //Ϊ��ǰ�̳߳�ʼ��Looper
			//����revHandler����
			revHandler = new Handler()
			{
				@Override
				public void handleMessage(Message msg) 
				{
					//���յ�UI�߳����û����������
					if (msg.what == 0x345)
					{
						//���û����ı��������������д������
						try 
						{
							outputStream.write((msg.obj.toString() + "\r\n").getBytes("utf-8"));
						} 
						catch (Exception e) 
						{
							e.printStackTrace();
						}
					}
					
				}
				
			};
			Looper.loop(); //����Looper
			
		} 
		catch (SocketTimeoutException e1)
		{
			System.out.println("�������ӳ�ʱ������");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
