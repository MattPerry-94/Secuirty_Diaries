package org.security_diaries;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.bdd_manager.DatabaseConnection;
import org.bdd_manager.DiaryManager;
import org.models.DiaryEntry;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DiariesController {

    @FXML
    private TextField NameEvent;
    @FXML
    private ComboBox<String> TypeEvent;
    @FXML
    private TextArea ContentEvent;
    @FXML
    private DatePicker BeginDate;
    @FXML
    private DatePicker EndDate;
    @FXML
    private Spinner<Integer> BeginHour;
    @FXML
    private Spinner<Integer> BeginMinute;
    @FXML
    private Spinner<Integer> EndHour;
    @FXML
    private Spinner<Integer> EndMinute;

    @FXML
    private TableView <DiaryEntry> diary;
    @FXML
    private TableColumn<DiaryEntry, String> type;
    @FXML
    private TableColumn<DiaryEntry, String> title;
    @FXML
    private TableColumn<DiaryEntry, String> begin;
    @FXML
    private TableColumn<DiaryEntry, String> end;

    private ObservableList<DiaryEntry> entries = FXCollections.observableArrayList();

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void initialize(){
        TypeEvent.setItems(FXCollections.observableArrayList(
                "Intrusion",
                "Levé de doute",
                "Incendie",
                "Accident"
        ));

        TypeEvent.setButtonCell(centeredListCell());
        TypeEvent.setCellFactory(listView -> centeredListCell());

        BeginHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        BeginMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        EndHour.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, 0));
        EndMinute.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0));
        BeginHour.setEditable(true);
        BeginMinute.setEditable(true);
        EndHour.setEditable(true);
        EndMinute.setEditable(true);
        BeginHour.getEditor().setAlignment(Pos.CENTER);
        BeginMinute.getEditor().setAlignment(Pos.CENTER);
        EndHour.getEditor().setAlignment(Pos.CENTER);
        EndMinute.getEditor().setAlignment(Pos.CENTER);
        BeginDate.setEditable(true);
        EndDate.setEditable(true);
        BeginDate.getEditor().setAlignment(Pos.CENTER);
        EndDate.getEditor().setAlignment(Pos.CENTER);
        NameEvent.setAlignment(Pos.CENTER);
        ContentEvent.setStyle("-fx-text-alignment: center;");

        type.setCellValueFactory(new PropertyValueFactory<>("type"));
        title.setCellValueFactory(new PropertyValueFactory<>("title"));
        begin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        end.setCellValueFactory(new PropertyValueFactory<>("end"));
        type.setStyle("-fx-alignment: CENTER;");
        title.setStyle("-fx-alignment: CENTER;");
        begin.setStyle("-fx-alignment: CENTER;");
        end.setStyle("-fx-alignment: CENTER;");

        diary.setItems(entries);

        reloadEntries();
    }
    @FXML
    public void AddEvent(){
        String name = NameEvent.getText();
        String type = TypeEvent.getValue();
        String content = ContentEvent.getText();
        LocalDate beginDate = BeginDate.getValue();
        LocalDate endDate = EndDate.getValue();
        Integer beginHour = BeginHour.getValue();
        Integer beginMinute = BeginMinute.getValue();
        Integer endHour = EndHour.getValue();
        Integer endMinute = EndMinute.getValue();

        if (beginDate == null || endDate == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Échec");
            alert.setHeaderText(null);
            alert.setContentText("Les dates de début et de fin sont obligatoires");
            alert.showAndWait();
            return;
        }

        LocalDateTime begin = LocalDateTime.of(beginDate, LocalTime.of(beginHour, beginMinute));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(endHour, endMinute));

        if (begin.isAfter(end)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Échec");
            alert.setHeaderText(null);
            alert.setContentText("La date de fin doit être après la date de début");
            alert.showAndWait();
            return;
        }

        boolean error = DiaryManager.addEntry(name, type, content, begin, end);
        if(!error){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Succès");
            alert.setHeaderText(null);
            alert.setContentText("L'entrée a bien été ajoutée !");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Échec");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ajouter !");
            alert.showAndWait();
        }




    }

    public void ReloadEvent() {
        reloadEntries();
    }

    @FXML
    public void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/hello-view.fxml"));
            Parent login = loader.load();

            Stage stage = (Stage) diary.getScene().getWindow();
            stage.setScene(new Scene(login));
            stage.setTitle("Journal de sécurité");
            stage.sizeToScene();
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void reloadEntries() {
        entries.clear();
        diary.setItems(entries);

        ResultSet rs = DiaryManager.getEntriesOfSevenDays();
        try {
            boolean found = false;
            while (rs.next()) {
                found = true;
                String id = rs.getString("id");
                String typeVal = rs.getString("type");
                String titre = rs.getString("title");
                String dateDebut = formatTimestamp(rs.getTimestamp("created_date"));
                String dateFin = formatTimestamp(rs.getTimestamp("finished_date"));
                String content = rs.getString("content");

                DiaryEntry entry = new DiaryEntry(id, typeVal, titre, dateDebut, dateFin, content);
                entries.add(entry);
            }

            if (!found) {
                System.out.println("Aucune entrée trouvée dans la base.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static ListCell<String> centeredListCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                setAlignment(Pos.CENTER);
            }
        };
    }

    private static String formatTimestamp(Timestamp ts) {
        if (ts == null) {
            return "";
        }
        return ts.toLocalDateTime().format(DATE_TIME_FORMATTER);
    }
}
