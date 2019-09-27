最近因为赶项目进度，因此将本来要用原生控件实现的界面，自己做了 H5 并嵌入 webview 中。发现点击 H5 中 input type="file" 标签 不能打开 android 资源管理器。

通过网络搜索发现是因为 android webview 由于考虑安全原因屏蔽了 input type="file" 这个功能 。

经过不懈的努力，以及 google 翻译的帮助 在 stackoverflow 中找到了解决的方法，具体可以理解为 重写 webview 的 WebChromeClient ，废话不多说直接贴代码：

```java
private ValueCallback<Uri> mUploadMessage;
public ValueCallback<Uri[]> uploadMessage;
public static final int REQUEST_SELECT_FILE = 100;
private final static int FILECHOOSER_RESULTCODE = 2;

webview.setWebChromeClient(new WebChromeClient(){

        // For 3.0+ Devices (Start)
        // onActivityResult attached before constructor
        protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
        {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
        }


        // For Lollipop 5.0+ Devices
        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
        {
            if (uploadMessage != null) {
                uploadMessage.onReceiveValue(null);
                uploadMessage = null;
            }

            uploadMessage = filePathCallback;

            Intent intent = fileChooserParams.createIntent();
            try
            {
                startActivityForResult(intent, REQUEST_SELECT_FILE);
            } catch (ActivityNotFoundException e)
            {
                uploadMessage = null;
                Toast.makeText(getBaseContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

        //For Android 4.1 only
        protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
        {
            mUploadMessage = uploadMsg;
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
        }

        protected void openFileChooser(ValueCallback<Uri> uploadMsg)
        {
            mUploadMessage = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
        }

    });
}

@Override
public void onActivityResult(int requestCode, int resultCode, Intent intent)
{

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
        if (requestCode == REQUEST_SELECT_FILE)
        {
            if (uploadMessage == null)
                return;
            uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
            uploadMessage = null;
        }
    }
    else if (requestCode == FILECHOOSER_RESULTCODE)
    {
        if (null == mUploadMessage)
            return;
        // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
        // Use RESULT_OK only if you're implementing WebView inside an Activity
        Uri result = intent == null || resultCode != MainActivity.RESULT_OK ? null : intent.getData();
        mUploadMessage.onReceiveValue(result);
        mUploadMessage = null;
    }
    else
        Toast.makeText(getBaseContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
}
```
