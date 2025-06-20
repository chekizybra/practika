package org.chekizybra.practika;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class managerTarifsControllerTest {
    @Test
    void testGetTarifTypeId() {
        managerProfileTarifsController ctrl = new managerProfileTarifsController();
        assertEquals(1, ctrl.getTarifTypeId("ТВ"));
        assertEquals(2, ctrl.getTarifTypeId("Интернет"));
        assertEquals(3, ctrl.getTarifTypeId("Комбо"));
    }
    @Test
    void testGetDeviceId() {
        managerProfileTarifsController ctrl = new managerProfileTarifsController();
        assertEquals(1, ctrl.getDeviceId("Телевизор"));
        assertEquals(2, ctrl.getDeviceId("Модем"));
        assertEquals(3, ctrl.getDeviceId("Роутер"));
    }
}

