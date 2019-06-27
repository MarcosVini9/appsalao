package com.example.appsalao;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.appsalao.modelo.Cliente;
import com.example.appsalao.webservice.Api;
import com.example.appsalao.webservice.RequestHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_GET_REQUEST = 1024;
    private static final int CODE_POST_REQUEST = 1025;

    EditText editTextIdCliente;
    EditText editTextCliente;
    EditText editTextHorario;
    EditText editTextCabeleireiro;
    EditText editTextData;
    Button buttonSalvar;
    ProgressBar progressBar;
    ListView listView;
    List<Cliente> clienteList;

    Boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.barraProgresso);
        listView = findViewById(R.id.listViewClientes);

        editTextIdCliente = findViewById(R.id.editTextIdCliente);
        editTextCliente = findViewById(R.id.editTextCliente);
        editTextHorario = findViewById(R.id.editTextHorario);
        editTextCabeleireiro = findViewById(R.id.editTextCabeleireiro);
        editTextData = findViewById(R.id.editTextData);
        buttonSalvar = findViewById(R.id.buttonSalvar);

        clienteList = new ArrayList<>();

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isUpdating){
                    updateCliente();
                }
                else{
                    createCliente();
                }
            }
        });


        readCliente();

    }

    private void createCliente(){
        String cliente = editTextCliente.getText().toString().trim();
        String horario = editTextHorario.getText().toString().trim();
        String cabeleireiro = editTextCabeleireiro.getText().toString().trim();
        String data = editTextData.getText().toString().trim();

        if(TextUtils.isEmpty(cliente)){
            editTextCliente.setError("Digite o nome cliente");
            editTextCliente.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(horario)){
            editTextHorario.setError("Digite o horario do cliente");
            editTextHorario.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(cabeleireiro)){
            editTextCabeleireiro.setError("Digite o cabeleireiro do cliente");
            editTextCabeleireiro.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(data)){
            editTextData.setError("Digite a data do cliente");
            editTextData.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("cliente", cliente);
        params.put("horario", horario);
        params.put("cabeleireiro", cabeleireiro);
        params.put("data", data);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_APPSALAO, params, CODE_POST_REQUEST);
        request.execute();

    }

    private void updateCliente(){
        String id = editTextIdCliente.getText().toString();
        String cliente = editTextCliente.getText().toString().trim();
        String horario = editTextHorario.getText().toString().trim();
        String cabeleireiro = editTextCabeleireiro.getText().toString().trim();
        String data = editTextData.getText().toString().trim();

        if(TextUtils.isEmpty(cliente)){
            editTextCliente.setError("Digite o nome cliente");
            editTextCliente.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(horario)){
            editTextHorario.setError("Digite o horário do cliente");
            editTextHorario.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(cabeleireiro)){
            editTextCabeleireiro.setError("Digite o cabeleireiro do cliente");
            editTextCabeleireiro.requestFocus();
            return;
        }
        if(TextUtils.isEmpty(data)){
            editTextData.setError("Digite a data do cliente");
            editTextData.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("cliente", cliente);
        params.put("horario", horario);
        params.put("cabeleireiro", cabeleireiro);
        params.put("data", data);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_APPSALAO, params, CODE_POST_REQUEST);
        request.execute();

        buttonSalvar.setText("Salvar");
        editTextCliente.setText("");
        editTextHorario.setText("");
        editTextCabeleireiro.setText("");
        editTextData.setText("");

        isUpdating = false;


    }

    private void readCliente() {
        PerformNetworkRequest request =  new PerformNetworkRequest(Api.URL_READ_APPSALAO, null, CODE_GET_REQUEST);
        request.execute();
    }

    private void deleteCliente(int id){
        PerformNetworkRequest request =  new PerformNetworkRequest(Api.URL_DELETE_APPSALAO+id, null, CODE_GET_REQUEST);
        request.execute();
    }



    private void refreshClienteList(JSONArray cliente) throws JSONException{
        clienteList.clear();

        for (int i = 0; i < cliente.length(); i++){
            JSONObject obj = cliente.getJSONObject(i);
            clienteList.add(new Cliente(
                    obj.getInt("id"),
                    obj.getString("cliente"),
                    obj.getString("horario"),
                    obj.getString("cabeleireiro"),
                    obj.getString("data")
            ));
        }
        ClienteAdapter adapter = new ClienteAdapter(clienteList);
        listView.setAdapter(adapter);
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"),Toast.LENGTH_SHORT).show();
                    refreshClienteList(object.getJSONArray("appadega"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }


    }

    class ClienteAdapter extends ArrayAdapter<Cliente>{

        List<Cliente> clienteList;

        public ClienteAdapter(List<Cliente> clienteList){
            super(MainActivity.this, R.layout.layout_cliente_list, clienteList);

            this.clienteList = clienteList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_cliente_list, null, true);

            TextView textViewServico = listViewItem.findViewById(R.id.textViewCliente);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);
            TextView textViewAlterar = listViewItem.findViewById(R.id.textViewAlterar);

            final Cliente cliente = clienteList.get(position);

            textViewServico.setText(cliente.getNomecliente());
            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                    builder.setTitle("Delete " + cliente.getNomecliente())
                            .setMessage("Você quer realmente deletar?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteCliente(cliente.getId());

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
            textViewAlterar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isUpdating = true;
                    editTextIdCliente.setText(String.valueOf(cliente.getId()));
                    editTextCliente.setText(String.valueOf(cliente.getNomecliente()));
                    editTextHorario.setText(String.valueOf(cliente.getHorariocliente()));
                    editTextCabeleireiro.setText(String.valueOf(cliente.getCabeleireirocliente()));
                    editTextData.setText(String.valueOf(cliente.getDatacliente()));
                    buttonSalvar.setText("Alterar");
                }
            });
            return listViewItem;

        }
    }


}