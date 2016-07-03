package com.goodeye.goodeye;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;






/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends AppCompatActivity {
    // TODO: remove this class when Haos class is ready
    public class MyAppInfoStorage extends AppInfoStorage {
        private void writeToFile(String fileName, String data) {
            try {
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName, Context.MODE_PRIVATE));
                outputStreamWriter.write(data);
                outputStreamWriter.close();
            }
            catch (IOException e) {
                Log.e("myTag", "File write failed: " + e.toString());
            }

        }

        private String readFromFile(String fileName) {

            String ret = "";

            try {
                InputStream inputStream = openFileInput(fileName);

                if ( inputStream != null ) {
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();

                    while ( (receiveString = bufferedReader.readLine()) != null ) {
                        stringBuilder.append(receiveString+"\n");
                    }

                    inputStream.close();
                    ret = stringBuilder.toString();
                }
            }
            catch (FileNotFoundException e) {
                Log.e("myTag", "File not found: " + e.toString());
            } catch (IOException e) {
                Log.e("myTag", "Can not read file: " + e.toString());
            }

            return ret;
        }
        public void UnlockID(String id) {
            id = id.trim().replace("\n", "");
//            Log.d("myTag", "UNLOCK ID: "+id);
            String s = readFromFile("unlocked.txt");
            if (!(s.isEmpty())) s += ";";
            s += id;
            writeToFile("unlocked.txt", s);
        }

        public boolean IsUnlockedID(String id) {
            String s = readFromFile("unlocked.txt");
            String[] unlocked = s.split(";");
            for (String temp : unlocked) {
                temp = temp.trim();
//                Log.d("myTag", "TEST: '"+id+"' ?== '"+temp+"'");
                if (temp.equals(id)) return true;
            }
            return false;
        }

        public List<String> GetAllUnlockedIDs() {
            String s = readFromFile("unlocked.txt");
            String[] unlocked = s.split(";");
            List<String> list = new ArrayList<String>();
            Collections.addAll(list, unlocked);
            return list;
        }

        public void SetValue(String name, String value) {
            // TODO implementation
        }

        public String GetValue(String name) {
            return "";
            // TODO implementation
        }
    }


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
//    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
//            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
//                    | View.SYSTEM_UI_FLAG_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    //==============================================================================================
    private class CustomWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //--------------------------------------------------------------------------------------
            // // TODO: remove this! 
            if (url.startsWith("myapp://")) {
                String message = url.replace("myapp://", "");
                //if (message.startsWith("SetButtonText:")) {
                    //    String[] parts = message.split(":");
                    //    Button mButton = (Button) findViewById(R.id.testButton02);
                    //    mButton.setText(parts[1]);
                //} else if(message.equals("LoadTestString")){
                if(message.equals("LoadTestString")){
                    MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"" + LoadString("books/book1/data.txt").replace("\n", "\n<br/>") + "\");");
                } else {
                    MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"" + message + "\");");
                }
                return true;
            }
            //--------------------------------------------------------------------------------------
            if (url.startsWith("goodeyeapi://")) {
//                Log.d("myTag", url);
                String message = url.replace("goodeyeapi://", "");
                String[] args = message.split("~~");
                if(args.length == 0){
                    return true;
                } else if(args[0].equals("openStartPage")) {
                    OpenStartPage();
                } else if(args[0].equals("buildStartPage")) {
                    BuildStartPage();
                //} else if(args[0].equals("initMenu")) {
                //    //Log.d("myTag", "TEST!!!");
                //    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"initMenu\");");
                //    InitMenu();
                } else if(args[0].equals("buildPage")) {
                    BuildPage();
                } else if(args[0].equals("openAchievementsMenu")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"openAchievementsMenu\");");
                    OpenAchievements();
                } else if(args[0].equals("openChallengesMenu")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"openChallengesMenu\");");
                    OpenGames();
                } else if(args[0].equals("openBook")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"openBook "+args[1]+"\");");
                    if (GetBookName(args[1]).equals("")) {
                        MyWebView.loadUrl("javascript:goodeyeAPI.error(\"book not found \\\""+args[1]+"\\\"\");");
                    } else {
                        OpenBook(args[1]);
                    }
                } else if(args[0].equals("openSection")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"openSection ["+args[1]+"] ["+args[2]+"]\");");
                    OpenSection(args[1], args[2]);
                } else if(args[0].equals("unlockSection")) { //TODO (???)
                    MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"unlockSection "+args[1]+" "+args[2]+"\");");
                } else if(args[0].equals("openChapter")) { //TODO (???)
                    MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"openChapter "+args[1]+"\");");
                } else if(args[0].equals("unlockChapter")) { //TODO (???)
                    MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"unlockChapter "+args[1]+"\");");
                } else if(args[0].equals("unlockNextChapter")) { //TODO (???)
                    MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"unlockNextChapter\");");
                } else if(args[0].equals("unlockGame")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"unlockGame "+args[1]+"\");");
                    UnlockGame(args[1]);
                } else if(args[0].equals("unlockAchievement")) {
                    UnlockAchievement(args[1]);
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"unlockAchievement "+args[1]+"\");");
                } else if(args[0].equals("nextPage")) {
                    NextPage();
                } else if(args[0].equals("previousPage")) { //TODO (???)
                    MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"previousPage\");");
                } else if(args[0].equals("openPage")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"openPage "+args[1]+"\");");
                    OpenPage(args[1]);
                } else if(args[0].equals("reloadPage")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"reloadPage\");");
                    ReloadPage();
                } else if(args[0].equals("message")) { //TODO (???)
                    MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"message "+args[1]+"\");");
                } else if(args[0].equals("loose")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"loose\");");
                    MyWebView.loadUrl("file:///android_asset/endscreen/index.html?type=loose");
                } else if(args[0].equals("win")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"win\");");
                    MyWebView.loadUrl("file:///android_asset/endscreen/index.html?type=win");
                } else if(args[0].equals("winStarRating")) {
                    //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"winStarRating "+args[1]+"\");");
                    MyWebView.loadUrl("file:///android_asset/endscreen/index.html?type=stars&stars="+args[1]);
                } else if(args[0].equals("winPercentage")) { //TODO !!! (???)
                    MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"winPercentage "+args[1]+"\");");
                } else {
                    MyWebView.loadUrl("javascript:goodeyeAPI.error(\"unknown command: "+args[0]+"\");");
                }
                return true;
            }

            return false;
        }
    }





    //==============================================================================================

    // TODO: this is just a debugging solution ... -> correct this when ready
    //public AppInfoStorage infoStorage = new AppInfoStorage();
    public MyAppInfoStorage infoStorage = new MyAppInfoStorage();

    public String menuTitle;
    public String menuIconPath;
    public String currentID;
    public String currentBook;
    public String currentSectionPath;
    public String nextSectionID;
    public List<String> currentPages = new ArrayList<String>();
    public int currentPageIndex;
    public Stack<String> modeStack = new Stack<String>();
    /*
     -> legal modeStack structures:
            []                        -> start_page
            [chapterView]             -> start_page/bookXY
            [chapterView,sectionView] -> start_page/bookXY/sectionXY (see currentBook for book id string)
     */

    String SectionID(String book, String chapter, String section) {
//        Log.d("myTag", "GET SECTION ID: "+"SECTION:books/"+book+"/"+chapter+"/"+section+"/");
        return "SECTION:books/"+book+"/"+chapter+"/"+section+"/";
    }


    //----------------------------------------------------------------------------------------------
    public void OpenStartPage() {
        //MyWebView.loadUrl("file:///android_asset/mainscreen/index.html");
        MyWebView.loadUrl("file:///android_asset/res/empty.html");
        //MyWebView.loadUrl("file:///android_asset/res/menu.html");
        buildPageTarget = "startPage";
        menuTitle = "Das gute Auge";
        menuIconPath = "file:///android_asset/res/eyeicon.png";
        modeStack.clear();
    }

    public void OpenBook(String bookID) {
        MyWebView.loadUrl("file:///android_asset/res/empty.html");
        buildPageTarget = "bookPage";
        menuTitle = GetBookName(bookID);
        menuIconPath = "file:///android_asset/res/book.png";
        currentID = bookID;
        modeStack.push("chapterView");
        modeStack.clear(); modeStack.push("chapterView");
    }

    public void OpenSection(String chapter, String section) {

        nextSectionID = "";
        String c = LoadString("books/"+currentBook+"/"+chapter+"/chapterinfo.txt");
        String[] clines = c.split("\n");
        boolean found = false;
        for (String l : clines) {
            l = l.trim();
            if (l.equals("")) continue;
            String[] args = l.split(":");
            if (args.length < 2) continue;
            if (args[0].trim().equals("section")) {
                if (args[1].trim().equals(section)) {
                    found = true;
                } else if (found) {
                    nextSectionID = SectionID(currentBook, chapter, args[1].trim());
                    //Log.d("myTag", "NEXT SECTION: "+SectionID(currentBook, chapter, args[1].trim()));
                    break;
                }
            }
        }


        currentSectionPath = "books/"+currentBook+"/"+chapter+"/"+section+"/";
        modeStack.clear(); modeStack.push("chapterView"); modeStack.push("sectionView");
        //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\""+currentSectionPath+"\");");

        currentPages.clear();
        String f = LoadString(currentSectionPath+"sectioninfo.txt");
        //f = f.replace("\n", "<br/>"); MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\""+f+"\");");
        String[] lines = f.split("\n");
        for (String l : lines) {
            l = l.trim();
            if (l.equals("")) continue;
            String[] args = l.split(":");
            if (args.length < 2) continue;
            if (args[0].trim().equals("page")) {
                currentPages.add(currentSectionPath+args[1].trim());
            }
        }

        currentPageIndex = 0;

        //----------------------------------------------------------------------
        /*
        // TODO: remove this
        MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"---\");");
        for (String p : currentPages) {
            MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"" + p + "\");");
        }
        */
        // ----------------------------------------------------------------------

        MyWebView.loadUrl("file:///android_asset/"+currentPages.get(currentPageIndex)+"/index.html");
        // TODO ...

    }


    public void OpenAchievements() {
        MyWebView.loadUrl("file:///android_asset/res/empty.html");
        buildPageTarget = "achievementsPage";
        menuTitle = "Achievements";
        menuIconPath = "file:///android_asset/res/achievements.png";
        modeStack.clear(); modeStack.push("achievementsView");
    }

    public void OpenGames() {
        MyWebView.loadUrl("file:///android_asset/res/empty.html");
        buildPageTarget = "gamesPage";
        menuTitle = "Challenges";
        menuIconPath = "file:///android_asset/res/challenges.png";
        modeStack.clear(); modeStack.push("challengesView");
    }


    //----------------------------------------------------------------------------------------------
    void NextPage() {
        if (!modeStack.isEmpty()) {
            if (modeStack.get(modeStack.size() - 1).equals("challengesView")) {
                OpenGames();
                return;
            }
        }

        currentPageIndex++;
        if(currentPageIndex >= currentPages.size()) {
            OpenBook(currentBook);
            if (!(nextSectionID.equals("")) && !(infoStorage.IsUnlockedID(nextSectionID))) {
                infoStorage.UnlockID(nextSectionID);
            }
            return;
        }
        //String sid = "SECTION:"+currentPages.get(currentPageIndex);
        //if (!infoStorage.IsUnlockedID(sid)) {
        //    infoStorage.UnlockID(sid);
        MyWebView.loadUrl("file:///android_asset/"+currentPages.get(currentPageIndex)+"/index.html");
        //}
    }

    void OpenPage(String pageID) {
        String pid = pageID;
        pageID = currentSectionPath+pageID;
        boolean found = false;
        for (int i=0; i<currentPages.size(); ++i) {
            if (currentPages.get(i).equals(pageID)) {
                found = true;
                currentPageIndex = i;
                break;
            }
        }
        if (!found) {
            MyWebView.loadUrl("javascript:goodeyeAPI.error(\"page not found: \\\""+pid+"\\\"\");");
            return;
        }
        MyWebView.loadUrl("file:///android_asset/"+currentPages.get(currentPageIndex)+"/index.html");
    }

    void ReloadPage() {
        MyWebView.reload();
    }
    //void ReloadPage() {
    //    MyWebView.loadUrl("file:///android_asset/"+currentPages.get(currentPageIndex)+"/index.html");
    //}

    // example: UnlockAchievement("helloWorld") -> unlocks "ACHIEVEMENT:achievements/helloWorld/"
    void UnlockAchievement(String achievementID) {
        achievementID = "ACHIEVEMENT:achievements/"+achievementID+"/";
        if(infoStorage.IsUnlockedID(achievementID)) return;
        infoStorage.UnlockID(achievementID);
    }

    void UnlockGame(String gameID) {
        gameID = "GAME:games/"+gameID+"/";
        if(infoStorage.IsUnlockedID(gameID)) return;
        infoStorage.UnlockID(gameID);
    }

    //----------------------------------------------------------------------------------------------
    public void BuildStartPage() {
        //Log.d("myTag", "----------");
        //Log.d("myTag", "building start page ...");
        //GetSubFolders("books");
        MyWebView.loadUrl("javascript:goodeyeAPI.setIcon(\""+menuIconPath+"\");");
        MyWebView.loadUrl("javascript:goodeyeAPI.setTitle(\""+menuTitle+"\");");
        List<String> list = GetSubFolders("books");
        for (String book : list) {
            //Log.d("myTag", "-->" + subDir);
            //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<a>"+book+"</a href='file:///android_asset/startscreen/index.html'> <br/>\");");
            MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<div class=\\\"iconButton\\\" onclick=\\\"goodeyeAPI.openBook('"+book+"')\\\"> <img src=\\\"file:///android_asset/res/book.png\\\"/><br/>"+GetBookName(book)+"</div>\");");
        }
        MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<div class=\\\"iconButton\\\" onclick=\\\"goodeyeAPI.openChallengesMenu()\\\"> <img src=\\\"file:///android_asset/res/challenges.png\\\"/><br/>challenges</div>\");");
        MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<div class=\\\"iconButton\\\" onclick=\\\"goodeyeAPI.openAchievementsMenu()\\\"> <img src=\\\"file:///android_asset/res/achievements.png\\\"/><br/>achievements</div>\");");

        //------------------------------------------------------------------------------------------
        // TODO: remove this!!!
        //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"test\");");
        //infoStorage.writeToFile("test.txt", "Hello\nWorld!!!");
        //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\"from test.txt: "+infoStorage.readFromFile("test.txt").replace("\n", "<br/>")+"\");");
//        infoStorage.UnlockID("TEST:hello/world/");
//        List<String> idList = infoStorage.GetAllUnlockedIDs();
//        Log.d("myTag", "---------------------------------------------------");
//        Log.d("myTag", "ALL UNLOCKED IDS:");
//        for (String id : idList) {
//            Log.d("myTag", id);
//        }
//        if (infoStorage.IsUnlockedID("TEST:hello/world/")) {
//            Log.d("myTag", "TEST:hello/world/ IS unlocked!");
//        } else {
//            Log.d("myTag", "TEST:hello/world/ is NOT unlocked!");
//        }
//        Log.d("myTag", "---------------------------------------------------");
        //------------------------------------------------------------------------------------------
    }

    public void BuildBookPage() {
        currentBook = currentID;
        MyWebView.loadUrl("javascript:goodeyeAPI.setIcon(\""+menuIconPath+"\");");
        MyWebView.loadUrl("javascript:goodeyeAPI.setTitle(\""+menuTitle+"\");");
        String info = LoadString("books/"+currentID+"/bookinfo.txt");
        String[] lines = info.split("\n");
        String tempContent = "";
        for (String l : lines) {
            l = l.trim();
            if (l.equals("")) continue;
            String[] args = l.split(":");
            boolean firstSection = true;
            if (args[0].trim().equals("chapter") && args.length >= 2) {
                String chapterID = args[1].trim();
                String chapterName = GetChapterName(currentID, chapterID);
                //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<p style='border: 3px solid red;'><div class='chapterBlock'><h2>"+chapterName+"</h2>\");");
                tempContent = "<div class='chapterBlock'><h2>"+chapterName+"</h2>";


                //--- read chapter sections ---
                String chapInfo = LoadString("books/"+currentID+"/"+chapterID+"/chapterinfo.txt");
                String[] chapLines = chapInfo.split("\n");
                for (String cl : chapLines) {
                    cl = cl.trim();
                    if (cl.equals("")) continue;
                    String[] chargs = cl.split(":");
                    if (chargs[0].trim().equals("section") && chargs.length >= 2) {
                        String sectionID = chargs[1].trim();
                        String sectionName = GetSectionName(currentID, chapterID, sectionID);
                        //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\""+sectionName+"</br>\");");
                        //if (firstSection || SectionUnlocked("SECTION."+currentID+"."+chapterID+"."+sectionName)) {
//                        Log.d("myTag", "TESTING SECTION ID: "+"SECTION:books/"+currentID+"/"+chapterID+"/"+sectionID+"/");
                        if (firstSection || infoStorage.IsUnlockedID("SECTION:books/"+currentID+"/"+chapterID+"/"+sectionID+"/")) {
                            //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<div class=\\\"iconButton\\\" onclick=\\\"goodeyeAPI.openSection()\\\"> <img src=\\\"file:///android_asset/res/section.png\\\"/><br/>" + sectionName + "</div>\");");
                            tempContent += "<div class=\\\"iconButton\\\" onclick=\\\"goodeyeAPI.openSection('"+chapterID+"','"+sectionID+"')\\\"> <img src=\\\"file:///android_asset/res/section.png\\\"/><br/>" + sectionName + "</div>";
                            if (firstSection) firstSection = false;
                        } else {
                            //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<div class=\\\"iconButton\\\"> <img src=\\\"file:///android_asset/res/locked.png\\\"/><br/>" + sectionName + "</div>\");");
                            tempContent += "<div class=\\\"iconButton\\\"> <img src=\\\"file:///android_asset/res/locked.png\\\"/><br/>" + sectionName + "</div>";
                        }
                    }
                }




                //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<div class=\\\"iconButton\\\" onclick=\\\"goodeyeAPI.openChapter('"+args[1].trim()+"')\\\"> <img src=\\\"file:///android_asset/res/eyeicon.png\\\"/><br/>"+GetChapterName(currentID, args[1].trim())+"</div>\");");
                //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\""+GetChapterName(currentID, args[1].trim())+"<br/>\")");

                //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"</div></p>\");");
                tempContent += "</div>";
                MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\""+tempContent+"\");");
                tempContent = "";
            }
        }
//        List<String> list = GetSubFolders("books/"+currentID+"/");
//        for (String chapter : list) {
//            //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<div class=\\\"iconButton\\\" onclick=\\\"goodeyeAPI.openBook('"+book+"')\\\"> <img src=\\\"file:///android_asset/res/book.png\\\"/><br/>"+GetBookName(book)+"</div>\");");
//            MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\""+chapter+"<br/>\")");
//        }
        //MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\"<div class=\\\"iconButton\\\" onclick=\\\"goodeyeAPI.openStartPage()\\\"> <img src=\\\"file:///android_asset/res/eyeicon.png\\\"/><br/>MainScreen</div>\");");
    }

    public void BuildAchievementsPage() {
        MyWebView.loadUrl("javascript:goodeyeAPI.setIcon(\""+menuIconPath+"\");");
        MyWebView.loadUrl("javascript:goodeyeAPI.setTitle(\""+menuTitle+"\");");
        //String tempContent = "Hello World!";
        String tempContent = "";

        tempContent += "<table>";
        List<String> allIDs = infoStorage.GetAllUnlockedIDs();
        //Log.d("myTag", "---------------------------");
        for (String id : allIDs) {
        //    Log.d("myTag", "id: "+id);
            if (id.startsWith("ACHIEVEMENT:")) {
                id = id.trim().replace("ACHIEVEMENT:", "");
                tempContent += "<tr><td><img src='file:///android_asset/"+id+"icon.png'/></td><td>";
                String ainfo = LoadString(id+"achievementinfo.txt");
        //        Log.d("myTag", "content of '"+id+"achievementinfo.txt"+"':\n"+ainfo);
                String[] alines = ainfo.split("\n");
                int mode = 0;
                String aname = id;
                String adescription = "";
                for (String l : alines) {
                    l = l.trim();
                    if (l.equals("")) continue;
                    if (l.equals("[name]")) {
                        mode = 1;
                        continue;
                    } else if (l.equals("[description]")) {
                        mode = 2;
                        continue;
                    }
                    switch (mode) {
                        case 1:
                            aname = l;
                            break;
                        case 2:
                            adescription += l+" ";
                            break;
                    }
                }
                tempContent += "<b>"+aname+"</b><p>"+adescription+"</p>";


                tempContent += "</td></tr>";
            }
        }
        tempContent += "</table>";

        MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\""+tempContent+"\");");
    }

    public void BuildGamesPage() {
        MyWebView.loadUrl("javascript:goodeyeAPI.setIcon(\""+menuIconPath+"\");");
        MyWebView.loadUrl("javascript:goodeyeAPI.setTitle(\""+menuTitle+"\");");
        //String tempContent = "Hello World!";
        String tempContent = "";

        List<String> allIDs = infoStorage.GetAllUnlockedIDs();
        //Log.d("myTag", "---------------------------");
        for (String id : allIDs) {
            //Log.d("myTag", "id: "+id);
            if (id.startsWith("GAME:")) {
                id = id.trim().replace("GAME:", "");

                String ainfo = LoadString(id+"gameinfo.txt");
                ainfo = ainfo.trim();
                String[] args = ainfo.split(":");
                String gameName = "NoName";
                if (args.length >= 2) gameName = args[1].trim();

                tempContent += "<div class=\\\"iconButton\\\" onclick=\\\"location.href='file:///android_asset/"+id+"index.html';\\\"> <img src=\\\"file:///android_asset/"+id+"icon.png\\\"/><br/>" + gameName + "</div>";
            }
        }



        MyWebView.loadUrl("javascript:goodeyeAPI.contentAdd(\""+tempContent+"\");");
    }

    //public boolean SectionUnlocked(String sectionID) {
    //    //=================================================
    //    // TODO check database whether section is unlocked
    //    //=================================================
    //    return false;
    //}

    //----------------------------------------------------------------------------------------------
    public String buildPageTarget;
    public void BuildPage() {
        if (buildPageTarget.equals("startPage")) {
            BuildStartPage();
        } else if (buildPageTarget.equals("bookPage")) {
            BuildBookPage();
        } else if (buildPageTarget.equals("achievementsPage")) {
            BuildAchievementsPage();
        } else if (buildPageTarget.equals("gamesPage")) {
            BuildGamesPage();
        }
    }
    //==============================================================================================





    //----------------------------------------------------------------
    // this returns sub folders within an assets folder
    // example: GetSubFolders("books") returns all folders within
    //          the folder 'file:/android_asset/books/' but not the
    //          sub folders of these folders
    //----------------------------------------------------------------
    public List<String> GetSubFolders(String pathWithinAssets) {
        String [] list;
        List<String> ret = new ArrayList<String>();
        try {
            list = getAssets().list(pathWithinAssets);
            if (list.length > 0) {
                for (String file : list) {
                    if(getAssets().list(pathWithinAssets+"/"+file).length > 0) {
                        //Log.d("myTag", "->" + file);
                        ret.add(file);
                    }
                }
            //} else {
            //    Log.d("myTag", "->empty...");
            }
        } catch (IOException e) {
            //Log.d("myTag", "error...");
        }
        return ret;
    }

    public String GetBookName(String bookID) {
        String s = LoadString("books/"+bookID+"/bookinfo.txt");
        String[] lines = s.split("\n");
        for (String l : lines) {
            l = l.trim();

            if (l.startsWith("bookname")) {
                String[] args = l.split(":");
                if (args.length < 2) continue;
                return args[1].trim();
            }

        }
        return "";
    }

    public String GetChapterName(String bookID, String chapterID) {
        String s = LoadString("books/"+bookID+"/"+chapterID+"/chapterinfo.txt");
        String[] lines = s.split("\n");
        for (String l : lines) {
            l = l.trim();

            if (l.startsWith("chaptername")) {
                String[] args = l.split(":");
                if (args.length < 2) continue;
                return args[1].trim();
            }

        }
        return "";
    }

    public String GetSectionName(String bookID, String chapterID, String sectionID) {
        String s = LoadString("books/"+bookID+"/"+chapterID+"/"+sectionID+"/sectioninfo.txt");
        //MyWebView.loadUrl("javascript:goodeyeAPI.debugLog(\""+"books/"+bookID+"/"+chapterID+"/"+sectionID+"/sectioninfo.txt"+"\");");
        String[] lines = s.split("\n");
        for (String l : lines) {
            l = l.trim();

            if (l.startsWith("sectionname")) {
                String[] args = l.split(":");
                if (args.length < 2) continue;
                return args[1].trim();
            }

        }
        return "";
    }


    public String LoadString(String path) {
        AssetManager assetManager = getBaseContext().getAssets();
        StringBuilder buf = new StringBuilder();
        try {
            InputStream txt = assetManager.open(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(txt, "UTF-8"));
            String str;

            while ((str = in.readLine()) != null) {
                buf.append(str);
                buf.append("\n");
            }

            in.close();
        }catch (IOException e) {
            //log the exception
        }
        return buf.toString();
    }


    private WebView MyWebView;
    //==============================================================================================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_main);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
//        mContentView = findViewById(R.id.fullscreen_content);

        //==========================================================================================
        MyWebView = (WebView) findViewById(R.id.webView);
        //MyWebView.setWebViewClient(new WebViewClient());
        MyWebView.setWebViewClient(new CustomWebViewClient());
        // Enable Javascript
        WebSettings webSettings = MyWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

//        MyWebView.loadUrl("file:///android_asset/jstest1.html");
        //MyWebView.loadUrl("file:///android_asset/ziptest/test.zip/test.html");


        //OpenStartPage();
        MyWebView.loadUrl("file:///android_asset/startscreen/index.html");


        //==========================================================================================

        /*
        // Set up the user interaction to manually show or hide the system UI.
        mContentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggle();
            }
        });
        */
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
//        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    @Override
    public void onBackPressed() {
        if (modeStack.isEmpty()) {
            super.onBackPressed();  // optional depending on your needs
            return;
        }


        String mode = modeStack.pop();

        if (mode.equals("chapterView")) {
            OpenStartPage();
        } else if (mode.equals("sectionView")) {
            OpenBook(currentBook);
        } else {
            OpenStartPage();
        }
    }
}
