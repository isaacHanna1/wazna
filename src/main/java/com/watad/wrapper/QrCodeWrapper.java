package com.watad.wrapper;

import com.watad.dto.QRCodeDto;

import java.util.ArrayList;
import java.util.List;

public class QrCodeWrapper {

    private List<QRCodeDto> list = new ArrayList<>();

    public List<QRCodeDto> getList() {
        return list;
    }

    public void setList(List<QRCodeDto> list) {
        this.list = list;
    }

}
