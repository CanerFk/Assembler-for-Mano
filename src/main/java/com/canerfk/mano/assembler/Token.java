package com.canerfk.mano.assembler;

class Token {
    private final String label;
    private final String mnemonic;
    private final String operand;
    private final boolean indirect;

    public Token(String label, String mnemonic, String operand, boolean indirect) {
        this.label = label;
        this.mnemonic = mnemonic;
        this.operand = operand;
        this.indirect = indirect;
    }

    public String getLabel() {
        return label;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public String getOperand() {
        return operand;
    }
    public boolean isIndirect() {
        return indirect;
    }
    @Override
    public String toString() {
        return String.format("Token [Label: %s, Mnemonic: %s, Operand: %s]",
                label != null ? label : "None",
                mnemonic != null ? mnemonic : "None",
                operand != null ? operand : "None");
    }


}
