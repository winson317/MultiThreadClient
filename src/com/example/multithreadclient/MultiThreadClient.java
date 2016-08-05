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
	ClientThread clientThread; //�����������ͨ�ŵ����߳�

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
				if (msg.what == 0x123)   //�����Ϣ���������߳�
				{
					show.append("\n" + msg.obj.toString()); //����ȡ������׷����ʾ���ı�����,����ʾ
				}
				//super.handleMessage(msg);
			}
        	
        };
        clientThread = new ClientThread(handler);
        new Thread(clientThread).start(); //�ͻ�������ClientThread�̴߳����������ӡ���ȡ���Է�����������
        send.setOnClickListener(new OnClickListener() 
        {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try 
				{
					Message msg = new Message(); //���û����·��Ͱ�ť�󣬽��û���������ݷ�װ��Message��Ȼ���͸����̵߳�Handler
					msg.what = 0x345;
					msg.obj = input.getText().toString();
					clientThread.revHandler.sendMessage(msg);
					input.setText(""); //���input�ı���
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
				}
			}
		});
        
    }
}
