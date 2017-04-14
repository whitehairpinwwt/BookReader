package com.sweetsjie.readbook;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;




public class ListAllFileActivity extends ListActivity {
	
	
	private List<File> fileList;
	private Bundle bundle;
	private String fileNameKey = "fileName";
	private String nameString;
	private MydatabaseHelper mydatabaseHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		File path = android.os.Environment.getExternalStorageDirectory();
		File[] f = path.listFiles();
		
		fill(f);
	}

	//��ȡ�ļ��б�,������listView
	private void fill(File[] files) {
		fileList = new ArrayList<File>();
		
		//�ļ��б����
		for (File file : files) {
			if (isValidFileOrDir(file)) {
				fileList.add(file);
			}
		}
		ArrayAdapter<String> fileNameList = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1,
					fileToStrArr(fileList)  );
		
		//����ListActivity����ķ��� 
		//ͬsetContentView��Activity�����е�����
		setListAdapter(fileNameList);
	}
	
	//����Ƿ�Ϊ�Ϸ����ļ����������Ƿ�Ϊ·��
	private boolean isValidFileOrDir(File fileIn)
	{
		if (fileIn.isDirectory()) {
			return true;
		}
		else {
			//���ļ�����ΪСд���´������ַ���
			String fileNameLow = fileIn.getName().toLowerCase();
			if (fileNameLow.endsWith(".txt")) {
				return true;
			}
		}
		return false;
	}
	

	//��һ���ļ�����ת�����������ֹ��ɵ������� �����б�
	//�õ� getName()�õ��ļ��� 
	private String[] fileToStrArr(List<File> fl)
	{
		ArrayList<String> fnList = new ArrayList<String>();
		for (int i = 0; i < fl.size(); i++) {
			nameString = fl.get(i).getName();
			//Log.d("TAG",nameString);
			fnList.add(nameString);
		}
		return fnList.toArray(new String[0]);
	}
	
	//�б������Ķ���
	//1.������ļ��� �����û�ͼ �����ļ���
	//2.������ļ� ������� ��ת��Ļ(Activity)
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		HomeContentFragment homeContentFragment = new HomeContentFragment();

		//positionΪ������ڵ�λ�� ����������ȡ�ö�Ӧ�ļ�
		File file = fileList.get(position);
		if (file.isDirectory())
		{
			File[] f = file.listFiles();
			fill(f);  // �ػ��ļ��б� ���µ��ļ���
		}
		else {
			mydatabaseHelper = new MydatabaseHelper(this,"book.db",null,1);
			SQLiteDatabase db = mydatabaseHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("bookname",nameString);
			values.put("bookurl",file.getAbsolutePath());
			db.insert("localbook",null,values);

			Intent intent = new Intent();
			intent.setClass(ListAllFileActivity.this, MainActivity.class);
			intent.putExtra("refresh",true);
//			bundle = new Bundle();
//			bundle.putString(fileNameKey, file.getAbsolutePath());
//			intent.putExtras(bundle);
			//homeContentFragment.NAME=homeContentFragment.addBookName(homeContentFragment.NAME,nameString);
			//homeContentFragment.refreshGridView();
			//Log.d("TAG",fileNameKey);
			//Log.d("TAG",nameString);
			//Log.d("TAG",file.getAbsolutePath());
			///storage/emulated/0/yysdk/��ȿռ�.txt
			startActivity(intent);
		}
	}
	
}
