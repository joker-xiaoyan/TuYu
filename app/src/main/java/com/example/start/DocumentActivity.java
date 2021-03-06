package com.example.start;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qmuiteam.qmui.skin.QMUISkinManager;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.popup.QMUIPopups;
import com.qmuiteam.qmui.widget.popup.QMUIQuickAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;



public class DocumentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapterForPicture myRecyclerviewAdapter;
    private List<FormImg> listAll = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document);




        //Toolbar
        Toolbar toolbarr = findViewById(R.id.documentToolbar);
        setSupportActionBar(toolbarr);
        //设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        //显示返回键
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        //不显示标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);




        //recyclerView
        recyclerView = findViewById(R.id.myrecyclerview);
        //创建一个网格视图管理器
        GridLayoutManager manager = new GridLayoutManager(
                this, 4
        );
        //瀑布流
        //StaggeredGridLayoutManager manager = new
        //StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //设置管理器
        recyclerView.setLayoutManager(manager);

        myRecyclerviewAdapter = new RecyclerViewAdapterForPicture(listAll);
        myRecyclerviewAdapter.GetManger(manager);
        recyclerView.setAdapter(myRecyclerviewAdapter);
        //设置颜色分割线
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //recyclerView.addItemDecoration(new GridDivider(this, 20, this.getResources().getColor(R.color.black)));
        //通过列数设置Item间距

        int spanCount = 4; // 3 columns
        int spacing = 15; // 15px
        boolean includeEdge = false;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));


        //设置默认动画效果
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置RecyclerView的每一项的长按事件
        myRecyclerviewAdapter.setOnItemLongClickListener(new RecyclerViewAdapterForPicture.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(DocumentActivity.this, "没开始写", Toast.LENGTH_SHORT).show();
                ShowPops(view, position);
            }
        });

        //设置RecyclerView的每一项的点击事件
        myRecyclerviewAdapter.setOnItemClickListener(new RecyclerViewAdapterForPicture.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ShowBigPicture(listAll.get(position).getmBitmap());
                Toast.makeText(DocumentActivity.this, "没开始写", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 获取menu
        getMenuInflater().inflate(R.menu.menuindocument, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.AddPicture:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);//意图：文件浏览器
                intent.setType("*/*");//无类型限制
                //允许进行多选操作，并返回多个uri
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);//关键！多选参数
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, 1);
                return true;

            case R.id.home:
                finish();
                return true;



            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }




    protected void ShowPops(View v,int pos){
        QMUIPopups.quickAction(getBaseContext(),
                QMUIDisplayHelper.dp2px(getBaseContext(), 56),
                QMUIDisplayHelper.dp2px(getBaseContext(), 56))
                .shadow(true)
                .skinManager(QMUISkinManager.defaultInstance(getBaseContext()))
                .edgeProtection(QMUIDisplayHelper.dp2px(getBaseContext(), 20))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.icon_quick_action_share).text("分享").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                Toast.makeText(getBaseContext(), "分享成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                ))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.icon_quick_action_copy).text("重命名").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                showRenameDialog(pos);
                                Toast.makeText(getBaseContext(), "修改成功", Toast.LENGTH_SHORT).show();

                            }
                        }
                ))
                .addAction(new QMUIQuickAction.Action().icon(R.drawable.icon_popup_close_dark).text("删除").onClick(
                        new QMUIQuickAction.OnClickListener() {
                            @Override
                            public void onClick(QMUIQuickAction quickAction, QMUIQuickAction.Action action, int position) {
                                quickAction.dismiss();
                                Toast.makeText(getBaseContext(), "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        }
                )).show(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == DocumentActivity.RESULT_OK) {
            if (data.getData() != null) {
                //单次点击未使用多选的情况
                try {
                    Uri uri = data.getData();
                    transformUri(uri);
                    Toast.makeText(DocumentActivity.this, "选择照片", Toast.LENGTH_SHORT).show();
                    myRecyclerviewAdapter.notifyItemInserted(myRecyclerviewAdapter.getItemCount());
                    recyclerView.scrollToPosition(myRecyclerviewAdapter.getItemCount());
                    //TODO 对获得的uri做解析，这部分在另一篇文章讲解
                    //String path = getPath(getApplicationContext(),uri);
                    //TODO 对转换得到的真实路径path做相关处理
                    //imageView.setImageBitmap(transformUri(fileList, uri));
                } catch (Exception e) { }
            }
            else{
                //长按使用多选的情况
                ClipData clipData = data.getClipData();
                if (clipData != null) {
                    List<String> pathList=new ArrayList<>();
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        Uri uri = item.getUri();
                        //TODO 对获得的uri做解析，这部分在另一篇文章讲解
                        //String path = getPath(getApplicationContext(),uri);
                        //routers.add(path);
                        pathList.add(uri.toString());
                        System.out.println(uri.toString());
                        transformUri(uri);
                        myRecyclerviewAdapter.notifyItemInserted(myRecyclerviewAdapter.getItemCount());
                        recyclerView.scrollToPosition(myRecyclerviewAdapter.getItemCount());

                    }
                    Toast.makeText(DocumentActivity.this, "选择照片", Toast.LENGTH_SHORT).show();
                }
            }
            TransFormBitmap(listAll);
        }


    }

    //新增图片
    private void newManger(){
        myRecyclerviewAdapter.notifyItemInserted(myRecyclerviewAdapter.getItemCount());
        recyclerView.scrollToPosition(myRecyclerviewAdapter.getItemCount());
    }






    //将uri转化为bitmap并且将其保存在list中
    public void transformUri(Uri uri){

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
            FormImg display = new FormImg("null", "null", "null", bitmap);
            listAll.add(display);
        }catch (Exception c){

        }

    }

    //设置重命名时候的dialog
    public void showRenameDialog(final int position){
        String text = listAll.get(position).getName();
        final QMUIDialog.EditTextDialogBuilder builder = new QMUIDialog.EditTextDialogBuilder(this);
        builder.setTitle("修改关键字")
                .setPlaceholder(text)
                //设置点击外部时dialog是否关闭
                .setCanceledOnTouchOutside(true)
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .addAction("确认", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        CharSequence editText = builder.getEditText().getText();
                        //先判断命名是否已经存在
                        if (editText.length() == 0 ) {
                            //期望是dialog的动态刷新，目前以Toast的方式提示用户
                            Toast.makeText(basic.myActivity, R.string.toastForNone1, Toast.LENGTH_SHORT).show();
                            Toast.makeText(basic.myActivity, R.string.toastForNone2, Toast.LENGTH_SHORT).show();
                        } else {
                            listAll.get(position).Rename(editText.toString());
                            myRecyclerviewAdapter.notifyItemChanged(position);
                            //实时保存数据
                            dialog.dismiss();
                        }
                    }
                })

                .addAction("取消", new QMUIDialogAction.ActionListener() {

                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .show();
        return ;
    }


    //单击照片查看大图
    private void ShowBigPicture(Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(bitmap);
        final QMUIDialog.CustomDialogBuilder customDialogBuilder = new QMUIDialog.CustomDialogBuilder(this);
        customDialogBuilder.setLayout(R.layout.showbigpicture);
        ImageButton imageView = findViewById(R.id.showit);
        imageView.setImageDrawable(drawable);
    }


    //ForDisplay类型数据储存接口
    //传进来一个List，里面是ForDisplay的数据
    //可以看java文件里的ForDisplay类
    private void TransFormBitmap(List<FormImg> list){
        //todo
    }




}