package edu.upc.whatsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import org.glassfish.tyrus.client.ClientManager;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;

import javax.websocket.ClientEndpointConfig;
import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.Session;

import edu.upc.whatsapp.adapter.MyAdapter_messages;
import edu.upc.whatsapp.comms.RPC;
import entity.Message;

import static edu.upc.whatsapp.comms.Comms.ENDPOINT;
import static edu.upc.whatsapp.comms.Comms.gson;

public class e_MessagesActivity_websocket extends Activity {

  _GlobalState globalState;
  ProgressDialog progressDialog;
  private ListView conversation;
  private MyAdapter_messages adapter;
  private EditText input_text;
  private Button button;
  private boolean enlarged = false, shrunk = true;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.e_messages);
    globalState = (_GlobalState) getApplication();
    TextView title = (TextView) findViewById(R.id.title);
    title.setText("Talking with: " + globalState.user_to_talk_to.getName());
    setup_input_text();

    new fetchAllMessages_Task().execute(globalState.my_user.getId(), globalState.user_to_talk_to.getId());
    new Thread(new Runnable() {
      public void run() {
        connectToServer();
      }
    }).start();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    new Thread(new Runnable() {
      public void run() {
        disconnectFromServer();
      }
    }).start();
  }

  private Session session;

  private void connectToServer(){
    try {
      ClientManager client = ClientManager.createClient();
      client.connectToServer(new MyEndPoint(),
          ClientEndpointConfig.Builder.create().build(),
          URI.create(ENDPOINT));
    }
    catch (Exception e) {
      e.printStackTrace();
      sendMessageToHandler("error","connectToServer error");
      session = null;
    }
  }
  private void disconnectFromServer(){
    if(session!=null){
      try {
        session.close();
      } catch (IOException e) {
        e.printStackTrace();
        sendMessageToHandler("error","disconnectFromServer error");
      }
    }
  }
  //this is executed by an independent thread:
  public class MyEndPoint extends Endpoint {

    @Override
    public void onOpen(Session session, EndpointConfig EndpointConfig) {
      try {

        //...
        
        session.addMessageHandler(new MessageHandler.Whole<String>() {
                    @Override
                    public void onMessage(String message) {
                        System.out.println("websocket message received:  "+message);

                        //...

                    }
                });

      }
      catch (Exception e) {
        e.printStackTrace();
        sendMessageToHandler("error","onOpen error: "+e.getMessage());
      }
    }

    @Override
    public void onError(Session session, Throwable t) {
      t.printStackTrace();
      sendMessageToHandler("error","onError error: "+t.getMessage());
    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
      sendMessageToHandler("close","connection closed");
      e_MessagesActivity_websocket.this.session = null;
    }

  }

  private void sendMessageToHandler(String type, String content){
    android.os.Message msg = handler.obtainMessage();
    Bundle bundle = new Bundle();
    bundle.putCharSequence("type", type);
    bundle.putCharSequence("content", content);
    msg.setData(bundle);
    handler.sendMessage(msg);
  }

  Handler handler = new Handler() {
    @Override
    public void handleMessage(android.os.Message msg) {
      String type = msg.getData().getCharSequence("type").toString();
      String content = msg.getData().getCharSequence("content").toString();
      if(type.equals("message")){

        //...

      }
      else{
        toastShow(content);
      }
    }
  };

  private class fetchAllMessages_Task extends AsyncTask<Integer, Void, List<Message>> {

    @Override
    protected void onPreExecute() {
      progressDialog = ProgressDialog.show(e_MessagesActivity_websocket.this,
          "MessagesActivity", "downloading messages...");
    }

    @Override
    protected List<Message> doInBackground(Integer... userIds) {

      //...

      //remove this sentence on completing the code:
      return null;
    }

    @Override
    protected void onPostExecute(List<Message> all_messages) {
      progressDialog.dismiss();
      if (all_messages == null) {
        toastShow("There's been an error downloading the messages");
      } else {
        toastShow(all_messages.size()+" messages downloaded");

        //...

      }
    }
  }

  private class fetchNewMessages_Task extends AsyncTask<Integer, Void, List<Message>> {

    @Override
    protected List<Message> doInBackground(Integer... userIds) {

      //...

      //remove this sentence on completing the code:
      return null;
    }

    @Override
    protected void onPostExecute(List<Message> new_messages) {
      if (new_messages == null) {
        toastShow("There's been an error downloading new messages");
      } else {
        toastShow(new_messages.size()+" new message/s downloaded");

        //...

      }
    }
  }

  public void sendText(final View view) {

    //...

    input_text.setText("");

    //to hide the soft keyboard after sending the message:
    InputMethodManager inMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    inMgr.hideSoftInputFromWindow(input_text.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }
  private class SendMessage_Task extends AsyncTask<Message, Void, Message> {

    @Override
    protected void onPreExecute() {
      toastShow("sending message");
    }

    @Override
    protected Message doInBackground(Message... messages) {

      //...

      //remove this sentence on completing the code:
      return null;
    }

    @Override
    protected void onPostExecute(Message message_reply) {
      if (message_reply.getId()>=0) {
        toastShow("message sent");

        //...

      } else {
        toastShow("There's been an error sending the message");
      }
    }
  }

  private void setup_input_text(){

    input_text = (EditText) findViewById(R.id.input);
    button = (Button) findViewById(R.id.mybutton);
    button.setEnabled(false);

    //to be notified when the content of the input_text is modified:
    input_text.addTextChangedListener(new TextWatcher() {

      public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
      }

      public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
      }

      public void afterTextChanged(Editable arg0) {
        if (arg0.toString().equals("")) {
          button.setEnabled(false);
        } else {
          button.setEnabled(true);
        }
      }
    });
    //to program the send soft key of the soft keyboard:
    input_text.setOnEditorActionListener(new OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEND) {
          sendText(null);
          handled = true;
        }
        return handled;
      }
    });
    //to detect a change on the height of the window on the screen:
    input_text.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
      @Override
      public void onGlobalLayout() {
        int screenHeight = input_text.getRootView().getHeight();
        Rect r = new Rect();
        input_text.getWindowVisibleDisplayFrame(r);
        int visibleHeight = r.bottom - r.top;
        int heightDifference = screenHeight - visibleHeight;
        if (heightDifference > 50 && !enlarged) {
          LayoutParams layoutparams = input_text.getLayoutParams();
          layoutparams.height = layoutparams.height * 2;
          input_text.setLayoutParams(layoutparams);
          enlarged = true;
          shrunk = false;
          conversation.post(new Runnable() {
            @Override
            public void run() {
              conversation.setSelection(conversation.getCount() - 1);
            }
          });
        }
        if (heightDifference < 50 && !shrunk) {
          LayoutParams layoutparams = input_text.getLayoutParams();
          layoutparams.height = layoutparams.height / 2;
          input_text.setLayoutParams(layoutparams);
          shrunk = true;
          enlarged = false;
        }
      }
    });
  }

  private void toastShow(String text) {
    Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
    toast.setGravity(0, 0, 200);
    toast.show();
  }

}
