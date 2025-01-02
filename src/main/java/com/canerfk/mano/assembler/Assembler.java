package com.canerfk.mano.assembler;

import java.util.ArrayList;
import java.util.List;

public class Assembler {
    private final SymbolTable symbolTable;
    private final List<String> sourceCode;
    private final List<Code> codes;
    private static final int MAX_ADDRESS = 0xFFF;

    public Assembler(List<String> sourceCode) {
        this.symbolTable = new SymbolTable();
        this.sourceCode = sourceCode;
        this.codes = new ArrayList<>();
    }

    public void firstPass() {
        String address = "000";
        for (String line : sourceCode) {
            Token token = LineParser.parseLine(line);
            if (token == null) continue;

            if (token.getLabel() != null) {
                symbolTable.addSymbol(token.getLabel(), address);
            }

            if (token.getMnemonic() == null) continue;

            switch (token.getMnemonic().toUpperCase()) {
                case "ORG":
                    if (token.getOperand() == null) {
                        throw new IllegalArgumentException("Missing address for ORG: " + token.getMnemonic());
                    }
                    address = token.getOperand();
                    break;
                case "END":
                    return;
                default:
                    address = incrementAddress(address);
                    break;
            }
        }
    }

    public void secondPass() {
        String address = "000";
        for (String line : sourceCode) {
            Token token = LineParser.parseLine(line);
            if (token == null) continue;

            if (token.getMnemonic() == null) continue;

            switch (token.getMnemonic().toUpperCase()) {
                case "ORG":
                    if (token.getOperand() != null) {
                        address = token.getOperand();
                    }
                    continue;
                case "END":
                    return;
                case "DEC":
                case "HEX":
                    if (token.getOperand() != null) {
                        int value = token.getMnemonic().equalsIgnoreCase("DEC") ?
                                Integer.parseInt(token.getOperand()) :
                                Integer.parseInt(token.getOperand(), 16);
                        codes.add(new Code(address, value & 0xFFFF));
                        address = incrementAddress(address);
                    }
                    continue;
                default:
                    handleInstruction(token, address);
                    address = incrementAddress(address);
                    break;
            }
        }
    }

    private void handleInstruction(Token token, String address) {
        if (!Instructions.isInctruction(token.getMnemonic())) {
            throw new IllegalArgumentException("Invalid mnemonic: " + token.getMnemonic());
        }

        int machineInstruction = Instructions.getOpcode(token.getMnemonic());

        if (Instructions.getType(token.getMnemonic()) == InstructionType.MEMORY_REFERENCE) {
            if (token.getOperand() != null) {
                String operandAddress = symbolTable.contains(token.getOperand()) ?
                        symbolTable.getAddress(token.getOperand()) :
                        String.format("%03X", Integer.parseInt(token.getOperand(), 16));

                if (token.isIndirect()) {
                    machineInstruction |= 0x8000;
                }

                machineInstruction |= (Integer.parseInt(operandAddress, 16) & 0xFFF);
            } else if (Instructions.requiresOperand(token.getMnemonic())) {
                throw new IllegalArgumentException("Missing operand for mnemonic: " + token.getMnemonic());
            }
        }
        codes.add(new Code(address, machineInstruction));
    }

    public void printMachineCode() {
        System.out.println("\nMemory Map:");
        System.out.println("Address  | Machine Code (Hex) | Machine Code (Binary)");
        System.out.println("---------|-------------------|--------------------");
        for (Code code : codes) {
            System.out.printf("%s     | %04X             | %s%n",
                    code.address(),
                    code.machineCode(),
                    formatToBinary(code.machineCode()));
        }
    }
    public String getMachineCode() {
        StringBuilder result = new StringBuilder();
        for (Code code : codes) {
            result.append(String.format("%s     | %04X             | %s%n",
                    code.address(),
                    code.machineCode(),
                    formatToBinary(code.machineCode())));
        }
        return result.toString();
    }
    public String getHexCode(){
        StringBuilder result = new StringBuilder();
        for (Code code : codes) {
            result.append(String.format("%04X",code.machineCode()));
        }
        return result.toString();
    }
    public void printSymbolTable() {
        symbolTable.printTable();
    }

    public static String incrementAddress(String hexAddress) {
        int intAddress = Integer.parseInt(hexAddress, 16);
        if (intAddress >= MAX_ADDRESS) {
            throw new IllegalArgumentException("Address out of range! Maximum is 0xFFF.");
        }
        intAddress++;
        return String.format("%03X", intAddress);
    }

    public static String convertDecToHex(String decValue) {
        int intValue = Integer.parseInt(decValue);
        if (intValue < 0) {
            intValue = (1 << 16) + intValue;
        }
        return String.format("%04X", intValue & 0xFFFF);
    }

    private String formatToBinary(int value) {
        String binaryString = String.format("%16s",
                Integer.toBinaryString(value & 0xFFFF)).replace(' ', '0');

        StringBuilder formatted = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i += 4) {
            if (i > 0) {
                formatted.append(" ");
            }
            formatted.append(binaryString, i, i + 4);
        }
        return formatted.toString();
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }
}

record Code(String address, int machineCode) {
}