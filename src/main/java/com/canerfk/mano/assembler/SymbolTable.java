package com.canerfk.mano.assembler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    private final Map<String, String> symbols;

    public SymbolTable() {
        this.symbols = new HashMap<>();
    }

    public void addSymbol(String symbol, String address) {
        if (symbols.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol already exists: " + symbol);
        }
        symbols.put(symbol, address);
    }

    public String getAddress(String symbol) {
        if (symbols.containsKey(symbol)) {
            return symbols.get(symbol);
        } else {
            throw new IllegalArgumentException("Symbol not found: " + symbol);
        }
    }

    public void printTable() {
        System.out.println("Symbol Table:");
        for (Map.Entry<String, String> entry : symbols.entrySet()) {
            System.out.printf("Symbol: %s, Address: %s%n", entry.getKey(), entry.getValue());
        }
    }

    public List<SymbolTableEntry> getEntries() {
        List<SymbolTableEntry> entries = new ArrayList<>();
        for (Map.Entry<String, String> entry : symbols.entrySet()) {
            entries.add(new SymbolTableEntry(entry.getKey(), entry.getValue()));
        }
        return entries;
    }

    public boolean contains(String symbol) {
        return symbols.containsKey(symbol);
    }

    public record SymbolTableEntry(String symbol, String address) {}
}
