package com.canerfk.mano.assembler;


class LineParser {
    public static Token parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        String cleanLine = line.split(";", 2)[0].trim();
        if (cleanLine.isEmpty()) {
            return null;
        }

        String label = null;
        if (cleanLine.contains(",")) {
            String[] labelSplit = cleanLine.split(",", 2);
            label = labelSplit[0].trim();
            cleanLine = labelSplit[1].trim();
        }
        String mnemonic = null;
        String operand = null;
        boolean indirect = false;
        String[] parts = cleanLine.split("\\s+", 3); // hata eklenebilir!
        if (parts.length > 0) {
            mnemonic = parts[0];
        }
        if (parts.length > 1) {
            operand = parts[1].trim();
        }
        if (parts.length > 2 && parts[2].trim().equals("I")) {
            indirect = true;
        }

        return new Token(label, mnemonic, operand,indirect);

    }
}