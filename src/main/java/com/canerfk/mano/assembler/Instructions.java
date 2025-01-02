package com.canerfk.mano.assembler;

import java.util.HashMap;
import java.util.Map;

enum InstructionType {
    MEMORY_REFERENCE,
    REGISTER_REFERENCE,
    IO_REFERENCE
}

class Instructions {
    private static class InstructionDetail {
        String mnemonic;
        int opcode;
        InstructionType type;
        boolean requiresOperand;

        public InstructionDetail(String mnemonic, int opcode, InstructionType type, boolean requiresOperand) {
            this.mnemonic = mnemonic;
            this.opcode = opcode;
            this.type = type;
            this.requiresOperand = requiresOperand;
        }
    }

    private static final Map<String, InstructionDetail> instructions = new HashMap<>();

    static {
        initializeInstructions();
    }

    private static void initializeInstructions() {
        // memory reference instructions
        instructions.put("AND", new InstructionDetail("AND", 0x0000, InstructionType.MEMORY_REFERENCE, true));
        instructions.put("ADD", new InstructionDetail("ADD", 0x1000, InstructionType.MEMORY_REFERENCE, true));
        instructions.put("LDA", new InstructionDetail("LDA", 0x2000, InstructionType.MEMORY_REFERENCE, true));
        instructions.put("STA", new InstructionDetail("STA", 0x3000, InstructionType.MEMORY_REFERENCE, true));
        instructions.put("BUN", new InstructionDetail("BUN", 0x4000, InstructionType.MEMORY_REFERENCE, true));
        instructions.put("BSA", new InstructionDetail("BSA", 0x5000, InstructionType.MEMORY_REFERENCE, true));
        instructions.put("ISZ", new InstructionDetail("ISZ", 0x6000, InstructionType.MEMORY_REFERENCE, true));

        // register reference instructions
        instructions.put("CLA", new InstructionDetail("CLA", 0x7800, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("CLE", new InstructionDetail("CLE", 0x7400, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("CMA", new InstructionDetail("CMA", 0x7200, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("CME", new InstructionDetail("CME", 0x7100, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("CIR", new InstructionDetail("CIR", 0x7080, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("CIL", new InstructionDetail("CIL", 0x7040, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("INC", new InstructionDetail("INC", 0x7020, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("SPA", new InstructionDetail("SPA", 0x7010, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("SNA", new InstructionDetail("SNA", 0x7008, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("SZA", new InstructionDetail("SZA", 0x7004, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("SZE", new InstructionDetail("SZE", 0x7002, InstructionType.REGISTER_REFERENCE, false));
        instructions.put("HLT", new InstructionDetail("HLT", 0x7001, InstructionType.REGISTER_REFERENCE, false));

        // io reference instructions
        instructions.put("INP", new InstructionDetail("INP", 0xF800, InstructionType.IO_REFERENCE, false));
        instructions.put("OUT", new InstructionDetail("OUT", 0xF400, InstructionType.IO_REFERENCE, false));
        instructions.put("SKI", new InstructionDetail("SKI", 0xF200, InstructionType.IO_REFERENCE, false));
        instructions.put("SKO", new InstructionDetail("SKO", 0xF100, InstructionType.IO_REFERENCE, false));
        instructions.put("ION", new InstructionDetail("ION", 0xF080, InstructionType.IO_REFERENCE, false));
        instructions.put("IOF", new InstructionDetail("IOF", 0xF040, InstructionType.IO_REFERENCE, false));

    }

    public static int getOpcode(String mnemonic) {
        if (instructions.containsKey(mnemonic)) {
            return instructions.get(mnemonic).opcode;
        } else {
            throw new IllegalArgumentException("Invalid mnemonic: " + mnemonic);
        }
    }
    public static boolean isInctruction(String mnemonic) {
        return instructions.containsKey(mnemonic);
    }
    public static InstructionType getType(String mnemonic) {
        if (instructions.containsKey(mnemonic)) {
            return instructions.get(mnemonic).type;
        } else {
            throw new IllegalArgumentException("Invalid mnemonic: " + mnemonic);
        }
    }

    public static boolean requiresOperand(String mnemonic) {
        if (instructions.containsKey(mnemonic)) {
            return instructions.get(mnemonic).requiresOperand;
        } else {
            throw new IllegalArgumentException("Invalid mnemonic: " + mnemonic);
        }
    }
}