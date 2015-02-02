package com.syw.hongyunhonghe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.syw.hongyunhonghe.model.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;


public class UserInfoActivity extends Activity {

    private User user;

    // views
    EditText nameText;
    EditText companyText;
    EditText officeText;
    Button editInfoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // get user
        user = BmobUser.getCurrentUser(this, User.class);
        if (user == null) {
            finish();
        }

        getViews();

        // set views by user info
        if (user.getRealname() != null) {
            nameText.setText(user.getRealname());
        }
        if (user.getCompany() != null) {
            companyText.setText(user.getCompany());
        }
        if (user.getOffice() != null) {
            officeText.setText(user.getOffice());
        }

        attachButtonListeners();
    }


    // get views
    private void getViews() {
        nameText = (EditText)findViewById(R.id.info_name_text);
        companyText = (EditText)findViewById(R.id.info_company_text);
        officeText = (EditText)findViewById(R.id.info_office_text);
        editInfoButton = (Button)findViewById(R.id.info_edit_button);
    }

    // set button click listeners
    private void attachButtonListeners() {
        // edit info
        editInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                User user = BmobUser.getCurrentUser(v.getContext(), User.class);
                user.setRealname(nameText.getText().toString().trim());
                user.setCompany(companyText.getText().toString().trim());
                user.setOffice(officeText.getText().toString().trim());
                user.update(v.getContext(), user.getObjectId(),new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(v.getContext(), "修改信息成功", Toast.LENGTH_LONG).show();
                        BmobUser.logOut(v.getContext());
                        finish();
                    }
                    @Override
                    public void onFailure(int code, String msg) {
                        Toast.makeText(v.getContext(), "修改信息失败:" + msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
