package pl.mromasze.taccess;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordHashTest {
    public static void main(String[] args) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "haslo123";
        String hash = "$2a$10$O7PG0NeFhDXBVs4V9.4FDuMSEYTydnjUPsmUIunyBgkUSWvFVrF/O";
        
        System.out.println("Testing password: " + password);
        System.out.println("Against hash: " + hash);
        System.out.println("Match result: " + encoder.matches(password, hash));
        
        // Generate a new hash for comparison
        String newHash = encoder.encode(password);
        System.out.println("\nNew hash for same password: " + newHash);
        System.out.println("New hash matches: " + encoder.matches(password, newHash));
    }
}