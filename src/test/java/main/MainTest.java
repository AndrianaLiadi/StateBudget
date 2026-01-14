@Test
@DisplayName("Πλήρης ροή εκτέλεσης της Main (CLI Integration Test)")
void testMainCompleteFlow() {
    // Προσπάθησε να φορτώσεις από resources
    String csvPath = "test.csv";
    
    // Δημιούργησε το CSV αρχείο δυναμικά
    try {
        FileWriter writer = new FileWriter(csvPath);
        writer.write("code,name,type,amount,revenue_or_expenditure\n");
        writer.write("\"ΕΣΟΔΑ\",\"\",\"\",\"\",\"\"\n");
        writer.write("\"1001\",\"ΦΠΑ\",\"INCOME\",\"25.000.000\",\"revenue\"\n");
        writer.write("\"ΕΞΟΔΑ\",\"\",\"\",\"\",\"\"\n");
        writer.write("\"2001\",\"Μισθοί Δημόσιου\",\"EXPENSE\",\"12.000.000\",\"expenditure\"\n");
        writer.close();
    } catch (IOException e) {
        fail("Δεν μπόρεσε να δημιουργήσει test CSV: " + e.getMessage());
    }
    
    String input =
            "2024\n" +
            csvPath + "\n" +  // Χρησιμοποίησε το δημιουργημένο αρχείο
            "TestScenario\n" +
            "1001\n" +  // Αυτός ο κωδικός ΠΡΕΠΕΙ να υπάρχει στο CSV!
            "1300000\n" +
            "increase\n" +
            "n\n";

    System.setIn(new ByteArrayInputStream(input.getBytes()));
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    System.setOut(new PrintStream(output));

    assertDoesNotThrow(() -> Main.main(new String[]{}));
    
    // Καθάρισε
    new File(csvPath).delete();
}