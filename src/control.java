import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

public class control {
    private JTabbedPane controlTabs;
    public JPanel controlPanel;
    private JTextField studentNameInput;
    private JButton AddStudentEnterButton;
    private JButton AddStudentClearButton;
    private JLabel studentNumber;
    private JComboBox setQualificationComboBox; // enabled for students list
    private JTextField setQualificationTextField;
    private JButton setQualificationEnterButton;
    private JButton setQualificationClearButton;
    private JButton getQualificationButton;
    private JComboBox getQualificationComboBox; // enabled for students list
    private JLabel getQualificationLabel;
    private JTextArea studentsTextArea;
    private JButton showQualificationsButton;
    private JButton showOrderedQualificationsButton;

    private static JFrame frame = new JFrame("control");
    private Hashtable<String, String> students=new Hashtable<>();
    private ArrayList<String> studentsComboBoxList=new ArrayList<>();

    private void cleanInput(JTextField input){
        input.setText("");
        input.requestFocus();
    }

    private void insertRecordInAList(ArrayList<String> list, String record){
        list.add(record);
    }

    private boolean isItemInAList(ArrayList<String> list, String item){
        return list.contains(item);
    }

    private int getStudentsComboBoxListSize(){
        return studentsComboBoxList.size();
    }

    @SuppressWarnings({"unchecked"})
    private void updateComboBox(JComboBox comboBox){
        /* 1 list: the original one
        2 list: the one from the comboBox
        3 list the differences between both */

        int comboBoxCount = comboBox.getItemCount();
        System.out.println(comboBoxCount);
        ArrayList<String> comboBoxTmpList=new ArrayList<>(); // 2 list

        for (int i = 0; i < comboBoxCount; i++) {
            // updating 2 list (if any)
            String currentValue =  comboBox.getItemAt(i).toString();
            comboBoxTmpList.add(currentValue);
        }

        if (comboBoxTmpList.size() > 0) {
            // updating only differences, comparing elements from list 2 to list 1
            for (int i = 0; i < studentsComboBoxList.size(); i++) {
                String currentValue = studentsComboBoxList.get(i);
                if ( ! (comboBoxTmpList.contains(currentValue) ) ){
                    // list 2 does not contains value from list 1, updating comboBox
                    comboBox.addItem(currentValue);
                }
            }
        }
        else {
            // updating everything from list 1 to comboBoxes
            for (int j=0; j < studentsComboBoxList.size(); j++){
                String currentValue = studentsComboBoxList.get(j);
                comboBox.addItem(currentValue);
            }
        }
    }

    // Class constructor
    public control() {

        // ( ====== ADD STUDENT TAB LISTENERS ====== )
        AddStudentClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cleanInput(studentNameInput);
            }
        });
        AddStudentEnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentToInsert = studentNameInput.getText();

                // Check if the student to insert is empty
                if ( studentToInsert == null | studentToInsert.isEmpty() ) {
                    // inputField is empty
                    JOptionPane.showMessageDialog(frame,
                            "Please type a name","Insert error",  JOptionPane.ERROR_MESSAGE);
                    studentNameInput.requestFocus();
                    return;
                }

                Object[] options = {"Yes, please", "No way!"};
                int confirmation = JOptionPane.showOptionDialog(frame,
                        "Would you like to insert to: (" + studentToInsert + ")", "Confirm it",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                        null,     // Don't use a custom icon
                        options,  // Button's titles
                        options[0]); // Default button title

                if (confirmation == 0) {
                    // inputField is not empty
                    if( ! (isItemInAList(studentsComboBoxList, studentToInsert) ) ) {
                        // Both list does NOT contains the studentToInsert
                        insertStudent(studentToInsert); // Step 1: insert the student in Hashtable
                        JOptionPane.showMessageDialog(frame,
                                "The student: (" + studentToInsert + ") has been inserted correctly");
                        // Step 2: updating list
                        insertRecordInAList(studentsComboBoxList, studentToInsert);
                        updateStudentNumberLabel(); // Step 3: updating label to show student's total
                        // Step 4: update ComboBoxes (if any)
                        updateComboBox(setQualificationComboBox);
                        updateComboBox(getQualificationComboBox);

                    }
                    else {
                        JOptionPane.showMessageDialog(frame,
                                "The student (" + studentToInsert + ") already exists in the database",
                                "Insert error!r",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
                // Either Yes/No user choice the input must be deleted
                cleanInput(studentNameInput);
            }
        });
        controlTabs.addChangeListener(new ChangeListener() {
            @Override
            @SuppressWarnings({"unchecked" })
            public void stateChanged(ChangeEvent e) {
                // Check if the current tab is 1/2 from the index
                if (controlTabs.getSelectedIndex() == 1 || controlTabs.getSelectedIndex() == 2 || controlTabs.getSelectedIndex() == 3){
                    // Check if there is at least one student
                    if ( getStudentsComboBoxListSize() == 0 ) {
                        // The students database is empty
                        JOptionPane.showMessageDialog(frame,
                                "There is not students in the database, please fill out at least one",
                                "Student database empty",
                                JOptionPane.WARNING_MESSAGE);
                        controlTabs.setSelectedIndex(0); // Return to Add student tab
                        cleanInput(studentNameInput); // Set focus on the only one input
                    }
                    studentsTextArea.setEditable(false);
                }
            }
        });
        setQualificationEnterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentQualification = setQualificationTextField.getText();
                if ( !  (currentQualification == null | currentQualification.isEmpty() ) ) {
                    // InputField is not empty
                    String currentStudent = setQualificationComboBox.getSelectedItem().toString();

                    // Check if the qualification is in range of 1-10
                    if ( Integer.parseInt(currentQualification) >= 1 && Integer.parseInt(currentQualification) <= 10) {

                        // Double check before to insert the qualification
                        int confirmDialog = JOptionPane.showConfirmDialog(
                                frame,
                                "The qualification of (" + currentQualification + ") is OK for (" + currentStudent + ") ?" ,
                                "Are you sure ?",
                                JOptionPane.YES_NO_OPTION);

                        if ( confirmDialog == 0 ){
                            // Update qualification
                            students.put(currentStudent, setQualificationTextField.getText());
                            JOptionPane.showMessageDialog(frame,
                                    "Qualification updated for: " + currentStudent);
                        }

                    }
                    else {
                        JOptionPane.showMessageDialog(frame,
                                "Please enter a qualification between 1-10",
                                "Qualification Out of range",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
                else {
                    // inputField is empty
                    JOptionPane.showMessageDialog(frame,
                            "Please set a qualification",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
                // Clear inputField
                cleanInput(setQualificationTextField);
            }
        });
        setQualificationClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clean inputField
                cleanInput(setQualificationTextField);
            }
        });
        getQualificationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String student = getQualificationComboBox.getSelectedItem().toString();
                // Check if the current student has qualification
                if(students.get(student).equals("none")){
                    JOptionPane.showMessageDialog(frame,
                            "The student: (" + student + ") does NOT has qualification yet",
                            "Qualification missing ...",
                            JOptionPane.WARNING_MESSAGE);
                }
                else{
                    // Update getQualificationLabel
                    getQualificationLabel.setText(students.get(student));
                }

            }
        });
        showQualificationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringBuilder builder = new StringBuilder();
                builder.append("| Student - Qualification |\n");
                // FIND METHOD !!!
                for (String student: studentsComboBoxList){
                    String qualification = students.get(student);
                    if(qualification.equals("none")){
                        // Some student has not qualification yet
                        JOptionPane.showMessageDialog(frame,
                                String.format("The student: (%s) has not qualification yet, please set it one", student) ,
                                "Qualification missing ...",
                                JOptionPane.ERROR_MESSAGE);
                        controlTabs.setSelectedIndex(1);
                        setQualificationTextField.requestFocus();
                        return;
                    }
                    builder.append(String.format("| %s - %s |\n", student, qualification));
                }
                // Appending the table to studentsTextArea
                studentsTextArea.selectAll();
                studentsTextArea.replaceSelection(builder.toString());
            }
        });
        showOrderedQualificationsButton.addActionListener(new ActionListener() {
            @Override
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public void actionPerformed(ActionEvent e) {
                // BUBBLE SORT!!!

                System.out.println("original Hashtable: " + students);

                ArrayList<String> tmpList=new ArrayList<>();

                Set<String> keys = students.keySet();
                for(String key: keys){
                    String value = students.get(key);
                    // If some student does not have qualification, this will show a message and return
                    if(value.equals("none")){
                        JOptionPane.showMessageDialog(frame,
                                String.format("The student: (%s) has not qualification yet, please set it one", key) ,
                                "Qualification missing ...",
                                JOptionPane.ERROR_MESSAGE);
                        controlTabs.setSelectedIndex(1);
                        setQualificationTextField.requestFocus();
                        return;
                    }
                    tmpList.add(String.format("%s,%s", key, value));
                }

                System.out.println("original tmpList: " + tmpList.toString());

                // Bubble Sort
                boolean flag = true;
                while(flag){
                    int count = 0;
                    for(int i=0; i<tmpList.size(); i++){
                        try{
                            String currentValues = tmpList.get(i);
                            String nextValues = tmpList.get(i+1); // could be empty
                            String[] splitCurrentValues = currentValues.split(",");
                            String[] splitNextValues = nextValues.split(","); // could be empty
                            String currentKey = splitCurrentValues[0];
                            String currentValue = splitCurrentValues[1];
                            String nextKey = splitNextValues[0]; // could be empty
                            String nextValue = splitNextValues[1]; // could be empty

                            System.out.printf("iteration: %d\n", i);
                            System.out.printf("%s\n", tmpList.toString());
                            System.out.printf("currentValue(%s) | nextValue(%s) \n", currentValue, nextValue);
                            if(Integer.parseInt(currentValue) > Integer.parseInt(nextValue)){
                                System.out.println("currentValue is bigger than nextValue, swapping them ...");
                                // Swap
                                tmpList.set(i, nextValues);
                                tmpList.set(i+1, currentValues);
                                count +=1;
                            }

                        }
                        catch (IndexOutOfBoundsException err){
                            System.out.println("Warning: list out of range\n\n");
                        }
                    }
                    if(count==0){
                        flag=false;
                    }
                }

                studentsTextArea.selectAll();
                studentsTextArea.replaceSelection(tmpList.toString());


            }
        });
    }


    // Custom functions
    private void insertStudent(String student){
        // Insert a student
        students.put(student, "none");
    }

    private void updateStudentNumberLabel(){
        studentNumber.setText("Current students: " + getStudentsComboBoxListSize());
    }

    public void createFrame(){
        frame.setContentPane(new control().controlPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
