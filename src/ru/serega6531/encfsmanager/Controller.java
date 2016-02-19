package ru.serega6531.encfsmanager;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.DirectoryChooser;

import java.io.*;
import java.util.prefs.Preferences;

public class Controller {

    public void onMountClick(ActionEvent actionEvent) throws InterruptedException {
        Button mountButton = (Button) actionEvent.getSource();
        Button dismountButton = (Button) Main.scene.lookup("#dismountButton");
        PasswordField passEdit = (PasswordField) Main.scene.lookup("#passwordEdit");
        String pass = passEdit.getText();
        if(!pass.isEmpty()){
            try {
                ProcessBuilder pb = new ProcessBuilder("encfs", Main.encodeDir.getAbsolutePath(), Main.mountDir.getAbsolutePath());
                Process pr = pb.start();

                InputStream is = pr.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                InputStream es = pr.getErrorStream();
                InputStreamReader esr = new InputStreamReader(es);
                BufferedReader ebr = new BufferedReader(esr);
                String line;

                Thread.sleep(100);

                OutputStream out = pr.getOutputStream();
                OutputStreamWriter wout = new OutputStreamWriter(out);
                wout.write(pass);
                wout.flush();
                wout.close();

                while ((line = ebr.readLine()) != null) {
                    System.out.println("[E] " + line);
                }


                while ((line = br.readLine()) != null) {
                    System.out.println("[O] " + line);
                    if(line.contains("Error decoding volume key, password incorrect")) {
                        new Alert(Alert.AlertType.ERROR, "Password is incorrect").showAndWait();
                        return;
                    }
                }

                pr.waitFor();
                mountButton.setDisable(true);
                dismountButton.setDisable(false);
            } catch (IOException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getLocalizedMessage()).showAndWait();
            }
        } else
            new Alert(Alert.AlertType.ERROR, "Password field is empty").showAndWait();
    }


    public void onDismountClick(ActionEvent actionEvent) {
        try {
            Process pr = new ProcessBuilder("gnomesu", "umount", Main.mountDir.getAbsolutePath()).start();
            pr.waitFor();

            Button mountButton = (Button) Main.scene.lookup("#mountButton");
            Button dismountButton = (Button) actionEvent.getSource();
            mountButton.setDisable(false);
            dismountButton.setDisable(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onChooseMountClick(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        File f = dc.showDialog(Main.scene.getWindow());
        if(f != null){
            Label label = (Label) Main.scene.lookup("#mountLabel");
            label.setText("Mount point: " + f.getAbsolutePath());
            Main.mountDir = f;
            Preferences prefs = Preferences.userNodeForPackage(Main.class);
            prefs.put("mountDir", f.getAbsolutePath());
        }
    }

    public void onChooseEncodeClick(ActionEvent actionEvent) {
        DirectoryChooser dc = new DirectoryChooser();
        dc.setInitialDirectory(new File(System.getProperty("user.home")));
        File f = dc.showDialog(Main.scene.getWindow());
        if(f != null){
            Label label = (Label) Main.scene.lookup("#encodeLabel");
            label.setText("Encode dir: " + f.getAbsolutePath());
            Main.encodeDir = f;
            Preferences prefs = Preferences.userNodeForPackage(Main.class);
            prefs.put("encodeDir", f.getAbsolutePath());
        }
    }

}
