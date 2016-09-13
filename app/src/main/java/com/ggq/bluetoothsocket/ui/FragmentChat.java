package com.ggq.bluetoothsocket.ui;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ggq.bluetoothsocket.R;
import com.ggq.bluetoothsocket.ui.adapter.MyChatAdapter;
import com.ggq.bluetoothsocket.util.Constact;
import com.ggq.bluetoothsocket.util.UtilPool;
import com.ggq.bluetoothsocket.vo.MessageBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DYK on 2015/11/14.
 */
public class FragmentChat extends Fragment {

    int Enemy_Zan_Number=0;
    int I_Zan_Number=0;
    int I_Click=1;
    int Enemy_Click=1;
    int Round=1;
    int Enemy_Value=0;
    int I_Value=0;
    int Enemy_Blood=5000;
    int I_Blood=5000;
    int I_Money=200;
    int I_Grade=0;
    int Enemy_Grade=0;
    int I_Grade_Sign=0;
    int Enemy_Grade_Sign=0;
    int I_ZhongDu=0;
    int Enemy_ZhongDu=0;
    int I_QuanFang=0;
    int Enemy_QuanFang=0;
    int BigBo_lengque=10;
    int ChenMo_Sign=0;

    //装备标记
    int I_Attack_1=0;
    int I_Attack_2=0;


    private View contentView;

    //声明Button
    private Button bt_Send;
    private Button Zan;
    private Button SmallBo;
    private Button Start;
    private Button Fang;
    private Button MiddleBo;
    private Button BigBo;
    private Button Shop;
    private Button Normal_Attack;
    private Button Attack;

    private EditText ed_Write;
    private ListView lv_Chat;
    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    private TextView Enemy_Blood_TextView;
    private TextView I_Blood_TextView;
    private TextView Money_TextView;
    private TextView Enemy_Activity;
    private TextView I_Activity;
    private TextView I_Grade_TextView;
    private TextView Enemy_Grade_TextView;

    //物品栏
    private Button Goods_1;
    private Button Goods_2;
    private Button Goods_3;


    private BluetoothSocket mSocket;
    private UtilPool utilPool;
    private List<MessageBean> messageList = new ArrayList<MessageBean>();
    private MyChatAdapter myChatAdapter;
    String a;

    protected static final int SUCCESS_CONNECT = 0;
    protected static final int SUCCESS_ACCEPT = 1;
    protected static final int SUCCESS_EXCHANGE = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_chat, null);
        initView();
        return contentView;
    }

    private void Round_Result(){
        //双方均已作出决定，结算结果
        if(Enemy_Click==1&&I_Click==1)
        {
         textView2.setText("我方攒的个数："+I_Zan_Number);
            textView.setText("对方攒的个数："+Enemy_Zan_Number);

            if(I_Zan_Number==5){Zan.setText("回血");}
            else {Zan.setText("攒气");}

            if(I_Zan_Number==0)Shop.setText("道具");
                else Shop.setText("");

            if(Enemy_Value==1){Enemy_Activity.setText("攒气");}
            if(Enemy_Value==2){Enemy_Activity.setText("小波");}
            if(Enemy_Value==3){Enemy_Activity.setText("大波");}
            if(Enemy_Value==4){Enemy_Activity.setText("完杀");}
            if(Enemy_Value==5){Enemy_Activity.setText("防御");}
            if(Enemy_Value==6){Enemy_Activity.setText("回血");Enemy_Value=1;Enemy_Blood=Enemy_Blood+1500;}
            if(Enemy_Value==7){Enemy_Activity.setText("血药");Enemy_Value=1;if(Enemy_ZhongDu==0){Enemy_Value=1;Enemy_Blood=Enemy_Blood+300;}else Enemy_ZhongDu=0;}
            if(Enemy_Value==8){Enemy_Activity.setText("毒药");Enemy_Value=1;I_ZhongDu=5;}
            if(Enemy_Value==10){Enemy_Activity.setText("沉默");Enemy_Value=1;ChenMo_Sign=3;}

            if(I_Value==1){I_Activity.setText("攒气");}
            if(I_Value==2){I_Activity.setText("小波");}
            if(I_Value==3){I_Activity.setText("大波");}
            if(I_Value==4){I_Activity.setText("完杀");BigBo_lengque=10;}
            if(I_Value==5){I_Activity.setText("防御");}
            if(I_Value==6){I_Activity.setText("回血");I_Value=1;Zan.setText("攒气");I_Blood=I_Blood+1500;Toast.makeText(getActivity(), "血量+1500", Toast.LENGTH_SHORT).show();}
            if(I_Value==7){I_Activity.setText("血药");I_Value=1;if(I_ZhongDu==0){I_Blood=I_Blood+300;Toast.makeText(getActivity(), "我方血量+300", Toast.LENGTH_SHORT).show();}else I_ZhongDu=0;}
            if(I_Value==8){I_Activity.setText("毒药");I_Value=1;Enemy_ZhongDu=5;}
            if(I_Value==10){I_Activity.setText("沉默");I_Value=1;}


            //大招倒计时
            if(BigBo_lengque>0)
            {BigBo_lengque--;
            if(BigBo_lengque!=0)
            BigBo.setText("("+BigBo_lengque+")");
            else {BigBo.setText("完杀");}}


            //敌我方中毒处理
            if(I_ZhongDu!=0){I_Blood=I_Blood-100+5*I_Grade;I_ZhongDu--;Toast.makeText(getActivity(), "中毒血量-"+(100-10*I_Grade), Toast.LENGTH_SHORT).show();}
            if(Enemy_ZhongDu!=0){Enemy_Blood=Enemy_Blood-100+5*Enemy_Grade;Enemy_ZhongDu--;}

            //敌我方全防处理
            if(I_QuanFang!=0){I_QuanFang--;
                if(Enemy_Value==2||Enemy_Value==3)
                {Toast.makeText(getActivity(), "发动全防抵挡一次攻击", Toast.LENGTH_LONG).show();Enemy_Value=1;I_QuanFang=0;}
                if(I_QuanFang==0){
                if(Goods_1.getText().equals("全防"))Goods_1.setText("");
                else if(Goods_2.getText().equals("全防"))Goods_2.setText("");
                else Goods_3.setText("");}}
            if(Enemy_QuanFang!=0){Enemy_QuanFang--;if(I_Value==2||I_Value==3){Toast.makeText(getActivity(), "敌方发动全防，攻击无效", Toast.LENGTH_SHORT).show();I_Value=1;Enemy_QuanFang=0;}}


            //沉默处理
            if(ChenMo_Sign>0){ChenMo_Sign--;Normal_Attack.setText("");}
            else{Normal_Attack.setText("普通攻击");}


            Enemy_Click=0;
            I_Click=0;
            textView3.setText("第"+Round+"回合");
            Round++;
            //都是攒
            if(I_Value==1&&Enemy_Value==1){}
            //一方不为防御，一方小波
            if(I_Value==2&&Enemy_Value!=5){Enemy_Blood=Enemy_Blood-500-50*I_Grade+25*Enemy_Grade;I_Money=I_Money-(-40-4*I_Grade+2*Enemy_Grade);
                Toast.makeText(getActivity(), "小波 金币+"+-(-40-4*I_Grade+2*Enemy_Grade), Toast.LENGTH_SHORT).show();}
            if(I_Value!=5&&Enemy_Value==2){I_Blood=I_Blood-500-50*Enemy_Grade+25*I_Grade;
                Toast.makeText(getActivity(), "我方血量"+(-500-50*Enemy_Grade+25*I_Grade), Toast.LENGTH_SHORT).show();}
            //一方不为防御，一方大波
            if(I_Value==3&&Enemy_Value!=5){Enemy_Blood=Enemy_Blood-1200-120*I_Grade+60*Enemy_Grade;I_Money=I_Money-(-96-9*I_Grade+5*Enemy_Grade);
                Toast.makeText(getActivity(), "大波 金币+"+-(-96-9*I_Grade+5*Enemy_Grade), Toast.LENGTH_SHORT).show();}
            if(I_Value!=5&&Enemy_Value==3){I_Blood=I_Blood-1200-120*Enemy_Grade+60*I_Grade;
                Toast.makeText(getActivity(), "我方血量"+(-1200-120*Enemy_Grade+60*I_Grade), Toast.LENGTH_SHORT).show();}
            //一方防御
            if(I_Value==5&&Enemy_Value==2){Toast.makeText(getActivity(), "完美防御", Toast.LENGTH_SHORT).show();}
            if(I_Value==5&&Enemy_Value==3){Toast.makeText(getActivity(), "完美防御", Toast.LENGTH_SHORT).show();}
            if(I_Value==2&&Enemy_Value==5){Toast.makeText(getActivity(), "攻击无效", Toast.LENGTH_SHORT).show();}
            if(I_Value==3&&Enemy_Value==5){Toast.makeText(getActivity(), "攻击无效", Toast.LENGTH_SHORT).show();}
            //一方完杀
            if(I_Value==4){Enemy_Blood=Enemy_Blood-800-80*I_Grade+40*Enemy_Grade;I_Money=I_Money-(-64-6*I_Grade+3*Enemy_Grade);
                Toast.makeText(getActivity(), "完杀 金币+"+-(-64-6*I_Grade+3*Enemy_Grade), Toast.LENGTH_LONG).show();}
            if (Enemy_Value == 4) {I_Blood=I_Blood-800-80*Enemy_Grade+40*I_Grade;
                Toast.makeText(getActivity(), "我方血量"+(-800-80*Enemy_Grade+40*I_Grade), Toast.LENGTH_LONG).show();}

            I_Blood_TextView.setText("我方血量："+I_Blood);
            Enemy_Blood_TextView.setText("敌方血量:"+Enemy_Blood);
            Money_TextView.setText("金钱:"+I_Money);
            }
}


    private void initView() {
        utilPool = new UtilPool(BluetoothAdapter.getDefaultAdapter(), mHandler);
        utilPool.Accept();

        //将Button连接到界面
        Start=(Button)contentView.findViewById(R.id.Start);
        Zan=(Button)contentView.findViewById(R.id.Zan);
        Fang=(Button)contentView.findViewById(R.id.Fang);
        BigBo=(Button)contentView.findViewById(R.id.BigBo);
        Shop=(Button)contentView.findViewById(R.id.Shop);
        Normal_Attack=(Button)contentView.findViewById(R.id.Normal_Attack);
        Attack=(Button)contentView.findViewById(R.id.Attack);


        textView=(TextView)contentView.findViewById(R.id.textView);
        textView2=(TextView)contentView.findViewById(R.id.textView2);
        textView3=(TextView)contentView.findViewById(R.id.textView3);
        Enemy_Blood_TextView=(TextView)contentView.findViewById(R.id.Enemy_Blood);
        I_Blood_TextView=(TextView)contentView.findViewById(R.id.I_Blood);
        Money_TextView=(TextView)contentView.findViewById(R.id.Money);
        I_Activity=(TextView)contentView.findViewById(R.id.IActivity);
        Enemy_Activity=(TextView)contentView.findViewById(R.id.EnemyActivity);
        I_Grade_TextView=(TextView)contentView.findViewById(R.id.I_Grade);
        Enemy_Grade_TextView=(TextView)contentView.findViewById(R.id.Enemy_Grade);

        //物品栏
        Goods_1=(Button) contentView.findViewById(R.id.Goods_1);
        Goods_2=(Button) contentView.findViewById(R.id.Goods_2);
        Goods_3=(Button) contentView.findViewById(R.id.Goods_3);

        myChatAdapter = new MyChatAdapter(messageList, getActivity());


/************************************物品栏点击监听******************************************/
        Goods_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Goods_1.getText().equals("血药"))
                {
                    if (I_Click == 0)
                    { I_Click=1;
                        utilPool.Exchange(Constact.NowSocket,"AddBlood");
                        Goods_1.setText("");
                        I_Activity.setText("血药");
                        Enemy_Activity.setText("???");
                        I_Value=7;
                        textView2.setText("我方攒的个数："+I_Zan_Number);
                        textView.setText("等待对方");}
                    else{
                        Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                    }

                    Round_Result();
                }
                if(Goods_1.getText().equals("毒药"))
                {
                    if (I_Click == 0)
                    { I_Click=1;
                        utilPool.Exchange(Constact.NowSocket,"DuYao");
                        Goods_1.setText("");
                        I_Activity.setText("毒药");
                        Enemy_Activity.setText("???");
                        I_Value=8;
                        textView2.setText("我方攒的个数："+I_Zan_Number);
                        textView.setText("等待对方");}
                    else{
                        Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                    }

                    Round_Result();
                }
                if(Goods_1.getText().equals("解药"))
                {
                    if (I_Click == 0)
                    { I_Click=1;
                        utilPool.Exchange(Constact.NowSocket,"JieYao");
                        Goods_1.setText("");
                        I_Activity.setText("解药");
                        Enemy_Activity.setText("???");
                        I_Value=9;
                        textView2.setText("我方攒的个数："+I_Zan_Number);
                        textView.setText("等待对方");}
                    else{
                        Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                    }

                    Round_Result();
                }
                if(Goods_1.getText().equals("沉默"))
                {
                    if (I_Click == 0)
                    { I_Click=1;
                        utilPool.Exchange(Constact.NowSocket,"ChenMo");
                        Goods_1.setText("");
                        I_Activity.setText("沉默");
                        Enemy_Activity.setText("???");
                        I_Value=10;
                        textView2.setText("我方攒的个数："+I_Zan_Number);
                        textView.setText("等待对方");}
                    else{
                        Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                    }

                    Round_Result();
                }

            }
        });

        Goods_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Goods_2.getText().equals("血药"))
                {
                    if (I_Click == 0)
                    { I_Click=1;
                        utilPool.Exchange(Constact.NowSocket,"AddBlood");
                        Goods_2.setText("");
                        I_Activity.setText("血药");
                        Enemy_Activity.setText("???");
                        I_Value=7;
                        textView2.setText("我方攒的个数："+I_Zan_Number);
                        textView.setText("等待对方");}
                    else{
                        Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                    }

                    Round_Result();
                }
                if(Goods_2.getText().equals("毒药"))
                {
                    if (I_Click == 0)
                    { I_Click=1;
                        utilPool.Exchange(Constact.NowSocket,"DuYao");
                        Goods_2.setText("");
                        I_Activity.setText("毒药");
                        Enemy_Activity.setText("???");
                        I_Value=8;
                        textView2.setText("我方攒的个数："+I_Zan_Number);
                        textView.setText("等待对方");}
                    else{
                        Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                    }

                    Round_Result();
                }

                if(Goods_2.getText().equals("解药"))
                {
                    if (I_Click == 0)
                    { I_Click=1;
                        utilPool.Exchange(Constact.NowSocket,"JieYao");
                        Goods_2.setText("");
                        I_Activity.setText("解药");
                        Enemy_Activity.setText("???");
                        I_Value=9;
                        textView2.setText("我方攒的个数："+I_Zan_Number);
                        textView.setText("等待对方");}
                    else{
                        Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                    }

                    Round_Result();
                }
                if(Goods_2.getText().equals("沉默"))
                {
                    if (I_Click == 0)
                    { I_Click=1;
                        utilPool.Exchange(Constact.NowSocket,"ChenMo");
                        Goods_2.setText("");
                        I_Activity.setText("沉默");
                        Enemy_Activity.setText("???");
                        I_Value=10;
                        textView2.setText("我方攒的个数："+I_Zan_Number);
                        textView.setText("等待对方");}
                    else{
                        Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                    }

                    Round_Result();
                }
}
        });

        Goods_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (Goods_3.getText().equals("血药")) {
                        if (I_Click == 0) {
                            I_Click = 1;
                            utilPool.Exchange(Constact.NowSocket, "AddBlood");
                            Goods_3.setText("");
                            I_Activity.setText("血药");
                            Enemy_Activity.setText("???");
                            I_Value = 7;
                            textView2.setText("我方攒的个数：" + I_Zan_Number);
                            textView.setText("等待对方");
                        } else {
                            Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                        }

                        Round_Result();
                    }
                    if (Goods_3.getText().equals("毒药")) {
                        if (I_Click == 0) {
                            I_Click = 1;
                            utilPool.Exchange(Constact.NowSocket, "DuYao");
                            Goods_3.setText("");
                            I_Activity.setText("毒药");
                            Enemy_Activity.setText("???");
                            I_Value = 8;
                            textView2.setText("我方攒的个数：" + I_Zan_Number);
                            textView.setText("等待对方");
                        } else {
                            Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                        }

                        Round_Result();
                    }
                    if (Goods_3.getText().equals("解药")) {
                        if (I_Click == 0) {
                            I_Click = 1;
                            utilPool.Exchange(Constact.NowSocket, "JieYao");
                            Goods_3.setText("");
                            I_Activity.setText("解药");
                            Enemy_Activity.setText("???");
                            I_Value = 9;
                            textView2.setText("我方攒的个数：" + I_Zan_Number);
                            textView.setText("等待对方");
                        } else {
                            Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                        }

                        Round_Result();
                    }
                    if (Goods_3.getText().equals("沉默")) {
                        if (I_Click == 0) {
                            I_Click = 1;
                            utilPool.Exchange(Constact.NowSocket, "ChenMo");
                            Goods_3.setText("");
                            I_Activity.setText("沉默");
                            Enemy_Activity.setText("???");
                            I_Value = 10;
                            textView2.setText("我方攒的个数：" + I_Zan_Number);
                            textView.setText("等待对方");
                        } else {
                            Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_SHORT).show();
                        }

                        Round_Result();
                    }
            }
        });


        /*****************************商店监听器****************************************/
        Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Shop.getText().equals("道具"))
                { final CharSequence[] items = {"血药 30金币 回300血或解除中毒状态", "毒药 70金币 敌方中毒五回合", "全防 75金币 三回合内可完全防御一次伤害", "沉默 70金币 使用后的三回合敌方无法出普通攻击", "虚弱 50金币 从下回合开始连续三回合对方伤害+50%"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater = getLayoutInflater(null);
                builder.setTitle("商店").setIcon(R.drawable.abc_ab_share_pack_holo_dark)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (items[which].equals("血药 30金币 回300血或解除中毒状态")) {
                                    if (I_Money >= 30) {
                                        if (Goods_1.getText().equals("")) {
                                            Goods_1.setText("血药");
                                            I_Money = I_Money - 30;
                                        } else if (Goods_2.getText().equals("")) {
                                            Goods_2.setText("血药");
                                            I_Money = I_Money - 30;
                                        } else if (Goods_3.getText().equals("")) {
                                            Goods_3.setText("血药");
                                            I_Money = I_Money - 30;
                                        } else
                                            Toast.makeText(getActivity(), "物品栏已满", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getActivity(), "金币不足", Toast.LENGTH_SHORT).show();
                                }
                                if (items[which].equals("毒药 70金币 敌方中毒五回合")) {
                                    if (I_Money >= 70) {
                                        if (Goods_1.getText().equals("")) {
                                            Goods_1.setText("毒药");
                                            I_Money = I_Money - 70;
                                        } else if (Goods_2.getText().equals("")) {
                                            Goods_2.setText("毒药");
                                            I_Money = I_Money - 70;
                                        } else if (Goods_3.getText().equals("")) {
                                            Goods_3.setText("毒药");
                                            I_Money = I_Money - 70;
                                        } else
                                            Toast.makeText(getActivity(), "物品栏已满", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getActivity(), "金币不足", Toast.LENGTH_SHORT).show();
                                }
                                if (items[which].equals("全防 75金币 三回合内可完全防御一次伤害")) {
                                    if (I_Money >= 75) {
                                        if (Goods_1.getText().equals("")) {
                                            Goods_1.setText("全防");
                                            I_Money = I_Money - 75;
                                            I_QuanFang =3;
                                            utilPool.Exchange(Constact.NowSocket, "QuanFang");
                                        } else if (Goods_2.getText().equals("")) {
                                            Goods_2.setText("全防");
                                            I_Money = I_Money - 75;
                                            I_QuanFang =3;
                                            utilPool.Exchange(Constact.NowSocket, "QuanFang");
                                        } else if (Goods_3.getText().equals("")) {
                                            Goods_3.setText("全防");
                                            I_Money = I_Money - 75;
                                            I_QuanFang =3;
                                            utilPool.Exchange(Constact.NowSocket, "QuanFang");
                                        } else
                                            Toast.makeText(getActivity(), "物品栏已满", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getActivity(), "金币不足", Toast.LENGTH_SHORT).show();
                                }
                                if (items[which].equals("沉默 70金币 使用后的三回合敌方无法出普通攻击")) {
                                    if (I_Money >= 70) {
                                        if (Goods_1.getText().equals("")) {
                                            Goods_1.setText("沉默");
                                            I_Money = I_Money - 70;
                                        } else if (Goods_2.getText().equals("")) {
                                            Goods_2.setText("沉默");
                                            I_Money = I_Money - 70;
                                        } else if (Goods_3.getText().equals("")) {
                                            Goods_3.setText("沉默");
                                            I_Money = I_Money - 70;
                                        } else
                                            Toast.makeText(getActivity(), "物品栏已满", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getActivity(), "金币不足", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.show();

            }
            else Toast.makeText(getActivity(), "仅可在我方攒气为零时购买道具", Toast.LENGTH_SHORT).show();
            }
        });



        /*****************************武器监听器****************************************/
        Attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items={"木剑 50金币 攻击时使用，攻击力提升5%"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater =getLayoutInflater(null);
                builder.setTitle("攻击").setIcon(R.drawable.abc_ab_share_pack_holo_dark)
                        .setItems(items,new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog,int which){
                                if(items[which].equals("木剑 50金币 攻击时使用，攻击力提升5%")) {
                                    if (I_Money >= 50) {
                                        if (Goods_1.getText().equals("")) {
                                            Goods_1.setText("木剑");
                                            I_Money = I_Money - 50;
                                        } else if (Goods_2.getText().equals("")) {
                                            Goods_2.setText("木剑");
                                            I_Money = I_Money - 50;
                                        } else if (Goods_3.getText().equals("")) {
                                            Goods_3.setText("木剑");
                                            I_Money = I_Money - 50;
                                        } else
                                            Toast.makeText(getActivity(), "物品栏已满", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(getActivity(), "金币不足", Toast.LENGTH_SHORT).show();
                                }
                                if(items[which].equals("铁剑")) {

                                }
                            }
                        });
                builder.show();

            }
        });



        Normal_Attack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ChenMo_Sign==0){
                final CharSequence[] items={"大波","小波"};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                LayoutInflater inflater =getLayoutInflater(null);
                builder.setIcon(R.drawable.abc_ab_share_pack_holo_dark)
                        .setItems(items,new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog,int which){
                                if(items[which].equals("小波")){
                                    //如果当前回合我方未作出决定，则发出所做出的决定
                                    if (I_Click == 0&&I_Zan_Number>0)
                                    { I_Click=1;
                                        utilPool.Exchange(Constact.NowSocket,"SmallBo");
                                        I_Activity.setText("小波");
                                        Enemy_Activity.setText("???");
                                        I_Zan_Number--;
                                        I_Value=2;
                                        textView2.setText("我方攒的个数："+I_Zan_Number);
                                        textView.setText("等待对方");}
                                    else if(I_Click==0)
                                        Toast.makeText(getActivity(), "当前攒气数量不足", Toast.LENGTH_LONG).show();
                                        //否则弹出提示
                                    else{Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_LONG).show();}
                                    Round_Result();
                                }
                                if(items[which].equals("大波")){
                                    //如果当前回合我方未作出决定，则发出所做出的决定
                                    if (I_Click == 0&&I_Zan_Number>1)
                                    { I_Click=1;
                                        utilPool.Exchange(Constact.NowSocket,"MiddleBo");
                                        I_Activity.setText("大波");
                                        Enemy_Activity.setText("???");
                                        I_Zan_Number=I_Zan_Number-2;
                                        I_Value=3;
                                        textView2.setText("我方攒的个数："+I_Zan_Number);
                                        textView.setText("等待对方");}
                                    else if(I_Click==0)
                                        Toast.makeText(getActivity(), "当前攒气数量不足", Toast.LENGTH_LONG).show();
                                        //否则弹出提示
                                    else{Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_LONG).show();}
                                    Round_Result();
                                }
                            }
                        });
                builder.show();

            }
            else Toast.makeText(getActivity(), "我方被沉默无法发动", Toast.LENGTH_SHORT).show();
            }
        });



        //开始Button监听器
        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Goods_1.setText("");
                Goods_2.setText("");
                Goods_3.setText("");
                Enemy_Zan_Number=0;
                I_Zan_Number=0;
                I_Click=0;
                Enemy_Click=0;
                Round=1;
                Enemy_Value=0;
                I_Value=0;
                Enemy_Blood=5000;
                I_Blood=5000;
                I_Grade_TextView.setText("["+I_Grade+"]");
                Enemy_Grade_TextView.setText("["+Enemy_Grade+"]");
                textView2.setText("我方攒的个数："+I_Zan_Number);
                textView.setText("对方攒的个数："+Enemy_Zan_Number);
                textView3.setText("第"+Round+"回合");
                utilPool.Exchange(Constact.NowSocket,"Start");
            }
        });


        /***********************防御Button的监听器***************************/
        Fang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (I_Click == 0)
                { I_Click=1;
                    utilPool.Exchange(Constact.NowSocket,"Fang");
                    I_Activity.setText("防御");
                    Enemy_Activity.setText("???");
                    I_Value=5;
                    textView2.setText("我方攒的个数："+I_Zan_Number);
                    textView.setText("等待对方");}
                else{
                    Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_LONG).show();
                }

                Round_Result();
            }
        });


        //攒气Button的监听器
        Zan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (I_Click == 0&&I_Zan_Number<5)
                { I_Click=1;
                utilPool.Exchange(Constact.NowSocket,"Zan");
                    I_Activity.setText("攒气");
                    Enemy_Activity.setText("???");
                I_Zan_Number++;
                    I_Value=1;
                    textView2.setText("我方攒的个数："+I_Zan_Number);
                    textView.setText("等待对方");}
                else if(I_Click==0&&I_Zan_Number==5)
                {I_Click=1;
                    utilPool.Exchange(Constact.NowSocket,"Grade+1");
                    I_Activity.setText("回血");
                    Enemy_Activity.setText("???");
                    I_Zan_Number=0;
                    I_Value=6;
                    textView2.setText("我方攒的个数："+I_Zan_Number);
                    textView.setText("等待对方");
                }
                else{Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_LONG).show();}
                Round_Result();}});


        /****************************大波Button的监听器***********************************/
        BigBo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(BigBo.getText().equals("完杀"))
                //如果当前回合我方未作出决定，则发出所做出的决定
                {if (I_Click == 0&&I_Zan_Number>3)
                { I_Click=1;
                    utilPool.Exchange(Constact.NowSocket,"BigBo");
                    I_Activity.setText("完杀");
                    Enemy_Activity.setText("???");
                    I_Zan_Number=I_Zan_Number-4;
                    I_Value=4;
                    textView2.setText("我方攒的个数："+I_Zan_Number);
                    textView.setText("等待对方");}
                else if(I_Click==0)
                    Toast.makeText(getActivity(), "当前攒气数量不足", Toast.LENGTH_LONG).show();
                //否则弹出提示
                else{Toast.makeText(getActivity(), "我方已作出决定", Toast.LENGTH_LONG).show();}
                Round_Result();}
            else Toast.makeText(getActivity(), "大招正在冷却中，还有"+BigBo_lengque+"回合", Toast.LENGTH_LONG).show();
            }});




    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SUCCESS_CONNECT:
                    Toast.makeText(getActivity(), "connect OK", Toast.LENGTH_SHORT).show();
                    break;
                case SUCCESS_ACCEPT:
                    Toast.makeText(getActivity(), "accept OK", Toast.LENGTH_LONG).show();
                    break;
                case SUCCESS_EXCHANGE:
                    byte[] readBuf = (byte[]) msg.obj;
                    a=new String(readBuf,0,msg.arg1);//与接收有关

                    if(a.equals("Start"))
                    {Start.setText("开始");}

                    //如果对方出攒
                    if(a.equals("Zan"))
                    {Enemy_Zan_Number++;
                        textView.setText("对方已作出决定");
                        I_Activity.setText("???");
                        Enemy_Activity.setText("???");
                        Enemy_Click=1;
                        Enemy_Value=1;}

                    //如果对方升级
                    if(a.equals("Grade+1")){
                        Enemy_Zan_Number=0;
                        textView.setText("对方已作出决定");
                        I_Activity.setText("???");
                        Enemy_Activity.setText("???");
                        Enemy_Click=1;
                        Enemy_Value=6;
                    }

                    //如果对方出小波
                    if(a.equals("SmallBo"))
                    {Enemy_Zan_Number--;
                        textView.setText("对方已作出决定");
                        I_Activity.setText("???");
                        Enemy_Activity.setText("???");
                        Enemy_Click=1;
                        Enemy_Value=2;}

                    //如果对方出中波
                    if(a.equals("MiddleBo"))
                    {Enemy_Zan_Number=Enemy_Zan_Number-2;
                        textView.setText("对方已作出决定");
                        I_Activity.setText("???");
                        Enemy_Activity.setText("???");
                        Enemy_Click=1;
                        Enemy_Value=3;}

                    //如果对方出大波
                    if(a.equals("BigBo"))
                    {Enemy_Zan_Number=Enemy_Zan_Number-4;
                        textView.setText("对方已作出决定");
                        I_Activity.setText("???");
                        Enemy_Activity.setText("???");
                        Enemy_Click=1;
                        Enemy_Value=4;}

                    //如果对方出防御
                    if(a.equals("Fang"))
                    {
                        textView.setText("对方已作出决定");
                        I_Activity.setText("???");
                        Enemy_Activity.setText("???");
                        Enemy_Click=1;
                        Enemy_Value=5;}

                    //如果对方出血药
                    if(a.equals("AddBlood"))
                    {
                        textView.setText("对方已作出决定");
                        I_Activity.setText("???");
                        Enemy_Activity.setText("???");
                        Enemy_Click=1;
                        Enemy_Value=7;
                    }

                    //如果对方出毒药
                    if(a.equals("DuYao"))
                    {
                        textView.setText("对方已作出决定");
                        I_Activity.setText("???");
                        Enemy_Activity.setText("???");
                        Enemy_Click=1;
                        Enemy_Value=8;
                    }

                    //如果对方出全防
                    if(a.equals("QuanFang"))
                    {
                        Enemy_QuanFang=3;
                    }


                    //如果对方出沉默
                    if(a.equals("ChenMo"))
                    {
                        textView.setText("对方已作出决定");
                        I_Activity.setText("???");
                        Enemy_Activity.setText("???");
                        Enemy_Click=1;
                        Enemy_Value=10;
                    }

                    Round_Result();
                    break;

            }
        }
    };
}
