package com.example.teammatch;

import android.app.*;
import android.os.*;
import android.graphics.Color;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class MainActivity extends Activity {
    LinkedHashMap<String,Integer> teams = new LinkedHashMap<>();
    Spinner s1,s2; TextView result;
    android.content.SharedPreferences pref;

    String[][] seed = {
      {"ريال مدريد","10"},{"برشلونة","10"},{"أتليتكو مدريد","9"},{"ريال بيتيس","9"},{"إشبيلية","8"},{"أتلتيك بيلباو","8"},{"ريال سوسيداد","7"},{"سيلتا فيغو","7"},{"أوساسونا","6"},{"إسبانيول","6"},{"فياريال","5"},{"ألافيس","5"},{"رايو فاليكانو","4"},{"خيتافي","4"},{"ديبورتيفو","3"},{"ليفانتي","3"},{"فالنسيا","2"},{"مالقا","2"},{"راسينغ","1"},{"إلتشي","1"},
      {"مانشستر سيتي","10"},{"أرسنال","10"},{"تشيلسي","10"},{"ليفربول","9"},{"مانشستر يونايتد","9"},{"نيوكاسل","9"},{"إيفرتون","8"},{"توتنهام","8"},{"أستون فيلا","8"},{"ليدز","7"},{"برايتون","7"},{"سندرلاند","7"},{"كريستال بالاس","6"},{"برينتفورد","6"},{"بورنموث","5"},{"نوتينغهام","5"},{"هال سيتي","5"},{"فولهام","4"},{"كوفنتري","4"},{"إيبسويتش تاون","4"},
      {"الإنتر","10"},{"ميلان","10"},{"يوفنتوس","9"},{"نابولي","9"},{"روما","8"},{"لاتسيو","8"},{"أتالانتا","7"},{"كومو","7"},{"فيورنتينا","6"},{"أودينيزي","6"},{"ساسولو","5"},{"تورينو","5"},{"فروزينوني","4"},{"كالياري","4"},{"بولونيا","3"},{"ليتشي","3"},{"بارما","2"},{"مونزا","2"},{"جنوى","1"},{"فينيزيا","1"},
      {"بايرن ميونخ","10"},{"بروسيا دورتموند","10"},{"ليفركوزن","9"},{"لايبزيغ","8"},{"فيردر بريمن","8"},{"شتوتغارت","7"},{"إينتراخت","6"},{"شالكة","6"},{"فرايبورغ","5"},{"أوغسبورغ","5"},{"إلفيسبيرغ","4"},{"ماينتس","3"},{"كولن","3"},{"هوفينهايم","2"},{"يونيون برلين","2"},{"بادربورن","1"},{"بروسيا مونشنغلادباخ","1"},{"هامبورغ","1"},
      {"باريس سان جيرمان","10"},{"مارسيليا","10"},{"موناكو","9"},{"ليل","9"},{"ليون","8"},{"رين","7"},{"باريس","7"},{"ستراسبورغ","6"},{"بريست","5"},{"لوريان","5"},{"لنس","4"},{"أنجيه","3"},{"تروا","3"},{"لومان","2"},{"أوكسير","2"},{"لوهافر","1"},{"تولوز","1"},{"نيس","1"}
    };

    public void onCreate(Bundle b){
        super.onCreate(b);
        pref=getSharedPreferences("teams",0);
        load();
        LinearLayout box=new LinearLayout(this); box.setOrientation(LinearLayout.VERTICAL); box.setPadding(32,32,32,32);
        TextView title=t("⚽ تقييم المباراة",26); box.addView(title);
        TextView sub=t("اختر فريقين لمعرفة التقييم من 20",16); box.addView(sub);

        s1=new Spinner(this); s2=new Spinner(this);
        box.addView(s1); box.addView(s2);
        result=t("اختر الفريقين",24); result.setGravity(Gravity.CENTER); box.addView(result);

        Button add=new Button(this); add.setText("➕ إضافة / تعديل فريق"); box.addView(add);
        add.setOnClickListener(v->dialog());

        setContentView(box); refresh();
        AdapterView.OnItemSelectedListener l=new AdapterView.OnItemSelectedListener(){public void onNothingSelected(AdapterView<?> p){} public void onItemSelected(AdapterView<?> p,View v,int x,long id){calc();}};
        s1.setOnItemSelectedListener(l); s2.setOnItemSelectedListener(l);
    }
    TextView t(String x,int z){TextView v=new TextView(this);v.setText(x);v.setTextSize(z);v.setPadding(8,18,8,18);return v;}
    void load(){ if(pref.getBoolean("init",false)){for(String n:pref.getStringSet("names",new HashSet<>())) teams.put(n,pref.getInt(n,0));} else {for(String[] a:seed)teams.put(a[0],Integer.parseInt(a[1]));save();}}
    void save(){pref.edit().putBoolean("init",true).putStringSet("names",teams.keySet()).apply(); for(Map.Entry<String,Integer> e:teams.entrySet())pref.edit().putInt(e.getKey(),e.getValue()).apply();}
    void refresh(){String[] a=teams.keySet().toArray(new String[0]);ArrayAdapter<String> ad=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,a);s1.setAdapter(ad);s2.setAdapter(ad);if(a.length>1)s2.setSelection(1);}
    void calc(){if(s1==null||s2==null||s1.getSelectedItem()==null)return;int n=teams.get(s1.getSelectedItem().toString())+teams.get(s2.getSelectedItem().toString());String c=n>=18?"مباراة ممتازة ⭐":n>=14?"مباراة جيدة 👍":n>=10?"مباراة متوسطة":"مباراة سيئة";result.setText("التقييم: "+n+" / 20\n"+c);}
    void dialog(){LinearLayout l=new LinearLayout(this);l.setOrientation(LinearLayout.VERTICAL);l.setPadding(40,10,40,10);EditText n=new EditText(this);n.setHint("اسم الفريق");EditText r=new EditText(this);r.setHint("التقييم من 10");r.setInputType(2);l.addView(n);l.addView(r);new AlertDialog.Builder(this).setTitle("إضافة / تعديل فريق").setView(l).setPositiveButton("حفظ",(d,w)->{try{int x=Integer.parseInt(r.getText().toString());if(x>=0&&x<=10&&n.getText().length()>0){teams.put(n.getText().toString(),x);save();refresh();}}catch(Exception e){}}).setNegativeButton("إلغاء",null).show();}
}