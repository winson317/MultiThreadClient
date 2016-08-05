package com.example.multithreadclient;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MultiThreadClient extends Activity {
	
	private EditText input;
	private TextView show;
	private Button send;
	Handler handler;
	ClientThread clientThread; //定义与服务器通信的子线程

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        input = (EditText)findViewById(R.id.input);
        show = (TextView)findViewById(R.id.show);
        send = (Button)findViewById(R.id.send);
        
        handler = new Handler()
        {
			@Override
			public void handleMessage(Message msg) 
			{
				if (msg.what == 0x123)   //如果消息来自于子线程
				{
					show.append("\n" + msg.obj.toString()); //将读取的内容追加显示在文本框中,并显示
				}
				//super.handleMessage(msg);
			}
        	
        };
        clientThread = new ClientThread(handler);
        new Thread(clientThread).start(); //客户端启动ClientThread线程创建网络连接、读取来自服务器的数据
        send.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try 
				{
					Message msg = new Message(); //当用户按下发送按钮后，将用户输入的数据封装成Message，然后发送给子线程的Handler
					msg.what = 0x345;
					msg.obj = input.getText().toString();
					clientThread.revHandler.sendMessage(msg);
					input.setText(""); //清空input文本框
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
        
    }
}
