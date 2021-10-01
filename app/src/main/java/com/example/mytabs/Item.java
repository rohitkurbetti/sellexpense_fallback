package com.example.mytabs;

public class Item {
    int rate = 0;
    int qty = 0;
    int total = 0;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }


    public Item(int rate, int qty, int total) {
        this.rate = rate;
        this.qty = qty;
        this.total = total;
    }
}

