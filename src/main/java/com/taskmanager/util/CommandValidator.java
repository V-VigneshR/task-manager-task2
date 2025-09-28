package com.taskmanager.util;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * CommandValidator provides security validation for shell commands.
 * It prevents execution of potentially dangerous commands.
 */
@Component
public class CommandValidator {

    /**
     * List of dangerous commands that should be blocked
     */
    private static final List<String> DANGEROUS_COMMANDS = Arrays.asList(
            // File system operations
            "rm", "rmdir", "del", "delete", "format",
            // Network operations
            "wget", "curl", "nc", "netcat", "telnet",
            // System operations
            "sudo", "su", "chmod", "chown", "passwd",
            // Process operations
            "kill", "killall", "pkill",
            // Archive operations that could be dangerous
            "tar", "zip", "unzip", "gzip", "gunzip",
            // Scripting that could execute arbitrary code
            "bash", "sh", "zsh", "fish", "csh", "tcsh",
            "python", "python3", "perl", "ruby", "node", "java",
            // Database operations
            "mysql", "psql", "mongo", "redis-cli",
            // System information that could be sensitive
            "passwd", "shadow", "hosts"
    );

    /**
     * List of dangerous characters/patterns that should be blocked
     */
    private static final List<String> DANGEROUS_PATTERNS = Arrays.asList(
            "|", "&&", "||", ";", "`", "$(",
            ">", ">>", "<", "<<",
            "&", "!", "*", "?"
    );

    /**
     * Validates if a command is safe to execute
     *
     * @param command The shell command to validate
     * @return true if the command is safe, false otherwise
     */
    public boolean isCommandSafe(String command) {
        if (command == null || command.trim().isEmpty()) {
            return false;
        }

        String normalizedCommand = command.toLowerCase().trim();

        // Check for dangerous commands
        for (String dangerousCmd : DANGEROUS_COMMANDS) {
            if (normalizedCommand.startsWith(dangerousCmd + " ") ||
                    normalizedCommand.equals(dangerousCmd)) {
                return false;
            }
        }

        // Check for dangerous patterns
        for (String pattern : DANGEROUS_PATTERNS) {
            if (command.contains(pattern)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Gets the reason why a command is unsafe
     *
     * @param command The command to check
     * @return A string explaining why the command is unsafe, or null if it's safe
     */
    public String getUnsafeReason(String command) {
        if (command == null || command.trim().isEmpty()) {
            return "Command is null or empty";
        }

        String normalizedCommand = command.toLowerCase().trim();

        // Check for dangerous commands
        for (String dangerousCmd : DANGEROUS_COMMANDS) {
            if (normalizedCommand.startsWith(dangerousCmd + " ") ||
                    normalizedCommand.equals(dangerousCmd)) {
                return "Command contains dangerous operation: " + dangerousCmd;
            }
        }

        // Check for dangerous patterns
        for (String pattern : DANGEROUS_PATTERNS) {
            if (command.contains(pattern)) {
                return "Command contains dangerous pattern: " + pattern;
            }
        }

        return null; // Command is safe
    }

    /**
     * Lists all allowed safe commands for reference
     *
     * @return List of example safe commands
     */
    public List<String> getSafeCommandExamples() {
        return Arrays.asList(
                "echo Hello World",
                "date",
                "whoami",
                "pwd",
                "ls",
                "cat filename.txt",
                "head filename.txt",
                "tail filename.txt",
                "wc filename.txt",
                "sleep 5"
        );
    }
}