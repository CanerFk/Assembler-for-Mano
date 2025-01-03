# Assembler for Mano Computer
A JavaFX-based assembler for the Mano Basic Computer instruction set architecture. This application provides a modern IDE-like interface for writing, assembling, and analyzing Mano assembly code.

## Features

- **Modern Code Editor**
  - Syntax highlighting for Mano assembly language
  - Line numbers
  - Auto-save functionality
  - Code template support

- **Real-time Assembly**
  - Immediate feedback on assembly errors
  - Machine code generation
  - Symbol table generation
  - Address resolution

- **Multiple Views**
  - Assembly code editor
  - Machine code table (Address, Hex, Binary)
  - Symbol table view
  - Error console

- **File Operations**
  - New file creation
  - Open .asm files
  - Save assembly files
  - Export machine code in HEX format

- **Instruction Support**
  - Memory Reference Instructions (AND, ADD, LDA, STA, BUN, BSA, ISZ)
  - Register Reference Instructions (CLA, CLE, CMA, CME, CIR, CIL, INC, SPA, SNA, SZA, SZE, HLT)
  - I/O Instructions (INP, OUT, SKI, SKO, ION, IOF)

- **Multi-language Support**
  - English
  - Turkish

## Example Program

```assembly
ORG 100
START, LDA NUM    ; Load number
      ADD ONE     ; Add 1
      STA SUM     ; Store result
      HLT         ; Halt program
NUM,   DEC 10     ; Number to increment
ONE,   DEC 1      ; Constant 1
SUM,   DEC 0      ; Result location
END
```

## Building and Running

1. Make sure you have JDK 17+ and Maven installed
2. Clone the repository
3. Build the project:
```bash
mvn clean package
```
4. Run the application:
```bash
java -jar target/mano-1.0-SNAPSHOT.jar
```
## Download link for ready jar file
[Download .jar File](https://sourceforge.net/projects/assemblerformano/)

To run this JAR file, you'll need to have JDK (Java Development Kit) installed on your computer. Here's how to do it:

Install JDK
Make sure you have JDK installed. You can download it from the official website:

- [Download JDK from Oracle](https://www.oracle.com/java/technologies/javase-downloads.html)

- Alternatively, you can use [AdoptOpenJDK](https://adoptopenjdk.net/) for an open-source version.

JDK includes the Java Runtime Environment (JRE), so you don't need to install JRE separately.
## Credits

Developed by CanerFk 

2024
