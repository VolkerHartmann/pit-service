package edu.kit.datamanager.pit.pidlog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;

public class KnownPidTest {

    private static final Instant NOW = Instant.now();
    private static final Instant LATER = NOW.plus(1, ChronoUnit.DAYS);

    @Test
    void testContruction() {
        KnownPid p = new KnownPid();
        String pid = "test";
        p.setPid(pid);
        p.setCreated(NOW);
        p.setModified(LATER);
        assertEquals(pid, p.getPid());
        assertEquals(NOW, p.getCreated());
        assertEquals(LATER, p.getModified());
        System.out.println(p.toString());
    }

    @Test
    void testTrivialEquivalence() {
        KnownPid p = new KnownPid();
        assertNotEquals(null, p);
        KnownPid b = new KnownPid();
        assertEquals(p, b);
    }

    @Test
    void testEquivalence() {
        String pid = "test";
        KnownPid p = new KnownPid(pid, NOW, LATER);
        KnownPid b = new KnownPid(pid, NOW, LATER);
        assertEquals(p, b);
        assertEquals(p.hashCode(), b.hashCode());

        b.setModified(NOW);
        assertNotEquals(p, b);
        assertNotEquals(p.hashCode(), b.hashCode());

        b.setCreated(LATER);
        assertNotEquals(p, b);
        assertNotEquals(p.hashCode(), b.hashCode());
    }
}