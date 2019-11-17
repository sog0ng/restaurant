package com.example.reservation.item;

public class C_ListViewItem {


        //손님이 예약시 필요
        private int type;//???
        private String nickname;//예약 이름
        private String r_date;//날짜
        private String arrival_time;//
        private String c_is_accept;//승인여부, 승인시 1, 미승인 0(초기값 0)
        private String covers;//인원
        private String send_score;//고객이 가게한테 준 평점
        private String year;//??
        private String month;//?
        private String day;//?

        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }

        public String getR_date() { return r_date; }
        public void setR_date(String r_date) { this.r_date = r_date; }

        public String getArrival_time() { return arrival_time; }
        public void setArrival_time(String arrival_time) { this.arrival_time = arrival_time; }

        public String getC_is_accept() { return c_is_accept; }
        public void setC_is_accept(String c_is_accept) { this.c_is_accept = c_is_accept; }

        public String getCovers() { return covers; }
        public void setCovers(String covers) { this.covers = covers; }

        public String getSend_score() { return send_score; }
        public void setSend_score(String send_score) { this.send_score = send_score; }





}
