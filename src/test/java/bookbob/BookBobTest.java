package bookbob;

import bookbob.entity.Records;
import org.junit.jupiter.api.Test;

import bookbob.functions.CommandHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookBobTest {
    CommandHandler command = new CommandHandler();
    Records records = new Records();

    public BookBobTest() throws IOException {
    }

    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream standardOut = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    void testHelp() {
        command.help();
        assertEquals("+-----------+---------------------------------------+---------------------------------+\n" +
                        "| Action    | Format                                | Example                         |\n" +
                        "+-----------+---------------------------------------+---------------------------------+\n" +
                        "| Help      | help                                  | help                            |\n" +
                        "+-----------+---------------------------------------+---------------------------------+\n" +
                        "| Add       | add n/NAME ic/NRIC [p/PHONE_NUMBER]   | add n/James Ho ic/S9534567A     |\n" +
                        "|           | [d/DIAGNOSIS] [m/MEDICATION]          | p/91234567 d/Asthma m/Albuterol |\n" +
                        "|           | [ha/HOME_ADDRESS] [dob/DATE_OF_BIRTH] | ha/NUS-PGPR dob/13121995        |\n" +
                        "+-----------+---------------------------------------+---------------------------------+\n" +
                        "| List      | list                                  | list                            |\n" +
                        "+-----------+---------------------------------------+---------------------------------+\n" +
                        "| Find      | find NAME [KEYWORDS] OR               | find NRIC S1234                 |\n" +
                        "|           | find NRIC [KEYWORDS] OR               |                                 |\n" +
                        "|           | find PHONE_NUMBER [KEYWORDS] OR       |                                 |\n" +
                        "|           | find DIAGNOSIS [KEYWORDS] OR          |                                 |\n" +
                        "|           | find MEDICATION [KEYWORDS] OR         |                                 |\n" +
                        "|           | find HOME_ADDRESS [KEYWORDS] OR       |                                 |\n" +
                        "|           | find DATE_OF_BIRTH [KEYWORDS]         |                                 |\n" +
                        "+-----------+---------------------------------------+---------------------------------+\n" +
                        "| Delete    | delete NRIC                           | delete S9534567A                |\n" +
                        "+-----------+---------------------------------------+---------------------------------+\n" +
                        "| Save      | save(automatic)                       | save                            |\n" +
                        "+-----------+---------------------------------------+---------------------------------+\n" +
                        "| Retrieve/ | retrieve or import                    | retrieve                        |\n" +
                        "| Import    | (automatic)                           |                                 |\n" +
                        "+-----------+---------------------------------------+---------------------------------+\n" +
                        "| Exit      | exit                                  | exit                            |\n" +
                        "+-----------+---------------------------------------+---------------------------------+\n".trim(),
                outputStreamCaptor.toString().trim());
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    void testAdd() {
        command.add("add n/James-Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS-PGPR dob/13121995", records);
        assertEquals("Patient James-Ho with NRIC S9534567A added.".trim(), outputStreamCaptor.toString().trim());
    }

    @Test
    void testDelete() {
        command.add("add n/James-Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS-PGPR dob/13121995", records);
        command.delete("S9534567A", records);
        assertEquals("Patient James-Ho with NRIC S9534567A added.\n" +
                "Patient James-Ho, S9534567A, has been deleted.", outputStreamCaptor.toString().trim());
    }

    @Test
    void testList() {
        command.add("add n/James-Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS-PGPR dob/13121995", records);
        command.list(records);
        assertEquals("Patient James-Ho with NRIC S9534567A added.\n" +
                "Name: James-Ho, NRIC: S9534567A, Phone: 91234567, Diagnosis: Asthma, Medication: [Albuterol], " +
                "Address: NUS-PGPR, DOB: 13121995", outputStreamCaptor.toString().trim());
    }

    @Test
    void testFindName() {
        command.add("add n/James Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS PGPR dob/13121995", records);
        command.add("add n/John Doe ic/S1234567Z p/97654321 d/Fever m/Panadol ha/Hougang Green dob/13121995", records);
        command.find("n/james", records);
        assertEquals("Patient James Ho with NRIC S9534567A added." + System.lineSeparator() +
                "Patient John Doe with NRIC S1234567Z added." + System.lineSeparator() +
                "Matching patients:" + System.lineSeparator() +
                "Name: James Ho, NRIC: S9534567A, Phone: 91234567, Diagnosis: Asthma, Medication: [Albuterol], " +
                "Address: NUS PGPR, DOB: 13121995", outputStreamCaptor.toString().trim());
    }

    @Test
    void testFindIc() {
        command.add("add n/James Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS PGPR dob/13121995", records);
        command.add("add n/John Doe ic/S1234567Z p/97654321 d/Fever m/Panadol ha/Hougang Green dob/13121995", records);
        command.find("ic/S1234567Z", records);
        assertEquals("Patient James Ho with NRIC S9534567A added." + System.lineSeparator() +
                "Patient John Doe with NRIC S1234567Z added." + System.lineSeparator() +
                "Matching patients:" + System.lineSeparator() +
                "Name: John Doe, NRIC: S1234567Z, Phone: 97654321, Diagnosis: Fever, Medication: [Panadol], " +
                "Address: Hougang Green, DOB: 13121995", outputStreamCaptor.toString().trim());
    }

    @Test
    void testFindPhoneNumber() {
        command.add("add n/James Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS PGPR dob/13121995", records);
        command.add("add n/John Doe ic/S1234567Z p/97654321 d/Fever m/Panadol ha/Hougang Green dob/13121995", records);
        command.find("p/91234567", records);
        assertEquals("Patient James Ho with NRIC S9534567A added." + System.lineSeparator() +
                "Patient John Doe with NRIC S1234567Z added." + System.lineSeparator() +
                "Matching patients:" + System.lineSeparator() +
                "Name: James Ho, NRIC: S9534567A, Phone: 91234567, Diagnosis: Asthma, Medication: [Albuterol], " +
                "Address: NUS PGPR, DOB: 13121995", outputStreamCaptor.toString().trim());
    }
    @Test
    void testFindDiagnosis() {
        command.add("add n/James Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS PGPR dob/13121995", records);
        command.add("add n/John Doe ic/S1234567Z p/97654321 d/Fever m/Panadol ha/Hougang Green dob/13121995", records);
        command.find("d/Asthma", records);
        assertEquals("Patient James Ho with NRIC S9534567A added." + System.lineSeparator() +
                "Patient John Doe with NRIC S1234567Z added." + System.lineSeparator() +
                "Matching patients:" + System.lineSeparator() +
                "Name: James Ho, NRIC: S9534567A, Phone: 91234567, Diagnosis: Asthma, Medication: [Albuterol], " +
                "Address: NUS PGPR, DOB: 13121995", outputStreamCaptor.toString().trim());
    }
    @Test
    void testFindMedication() {
        command.add("add n/James Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS PGPR dob/13121995", records);
        command.add("add n/John Doe ic/S1234567Z p/97654321 d/Fever m/Panadol ha/Hougang Green dob/13121995", records);
        command.find("m/Albuterol", records);
        assertEquals("Patient James Ho with NRIC S9534567A added." + System.lineSeparator() +
                "Patient John Doe with NRIC S1234567Z added." + System.lineSeparator() +
                "Matching patients:" + System.lineSeparator() +
                "Name: James Ho, NRIC: S9534567A, Phone: 91234567, Diagnosis: Asthma, Medication: [Albuterol], " +
                "Address: NUS PGPR, DOB: 13121995", outputStreamCaptor.toString().trim());
    }
    @Test
    void testFindHomeAddress() {
        command.add("add n/James Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS PGPR dob/13121995", records);
        command.add("add n/John Doe ic/S1234567Z p/97654321 d/Fever m/Panadol ha/Hougang Green dob/13121995", records);
        command.find("ha/NUS PGPR", records);
        assertEquals("Patient James Ho with NRIC S9534567A added." + System.lineSeparator() +
                "Patient John Doe with NRIC S1234567Z added." + System.lineSeparator() +
                "Matching patients:" + System.lineSeparator() +
                "Name: James Ho, NRIC: S9534567A, Phone: 91234567, Diagnosis: Asthma, Medication: [Albuterol], " +
                "Address: NUS PGPR, DOB: 13121995", outputStreamCaptor.toString().trim());
    }
    @Test
    void testFindDateOfBirth() {
        command.add("add n/James Ho ic/S9534567A p/91234567 d/Asthma m/Albuterol ha/NUS PGPR dob/13121995", records);
        command.add("add n/John Doe ic/S1234567Z p/97654321 d/Fever m/Panadol ha/Hougang Green dob/13121995", records);
        command.find("dob/13121995", records);
        assertEquals("Patient James Ho with NRIC S9534567A added." + System.lineSeparator() +
                        "Patient John Doe with NRIC S1234567Z added." + System.lineSeparator() +
                        "Matching patients:" + System.lineSeparator() +
                        "Name: James Ho, NRIC: S9534567A, Phone: 91234567, Diagnosis: Asthma, Medication: [Albuterol], " +
                        "Address: NUS PGPR, DOB: 13121995" + System.lineSeparator() +
                        "Name: John Doe, NRIC: S1234567Z, Phone: 97654321, Diagnosis: Fever, Medication: [Panadol], " +
                        "Address: Hougang Green, DOB: 13121995",
                outputStreamCaptor.toString().trim());
    }
}