package com.djn.api.preparedstatement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PSDMLPartTest {
    @Test
    public void testInsert() {
        assertEquals(1, PSDMLPart.insert(), "数据插入失败");
    }

    @Test
    public void testUpdate() {
        assertEquals(1, PSDMLPart.update(), "更新失败");
    }

    @Test
    public void testDelete() {
        assertEquals(1, PSDMLPart.delete(), "删除失败");
    }
}
