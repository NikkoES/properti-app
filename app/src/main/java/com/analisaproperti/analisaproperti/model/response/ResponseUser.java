package com.analisaproperti.analisaproperti.model.response;

import com.google.gson.annotations.SerializedName;

public class ResponseUser {

    @SerializedName("status")
    String status;
    @SerializedName("data")
    Data data;

    public ResponseUser(String status, Data data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public Data getData() {
        return data;
    }

    public static class Data{
        @SerializedName("id_user")
        String idUser;
        @SerializedName("nama_user")
        String namaUser;
        @SerializedName("no_hp")
        String noHp;
        @SerializedName("email")
        String email;
        @SerializedName("password")
        String password;
        @SerializedName("img_profile")
        String imgProfile;

        public Data(String idUser, String namaUser, String noHp, String email, String password, String imgProfile) {
            this.idUser = idUser;
            this.namaUser = namaUser;
            this.noHp = noHp;
            this.email = email;
            this.password = password;
            this.imgProfile = imgProfile;
        }

        public String getIdUser() {
            return idUser;
        }

        public String getNamaUser() {
            return namaUser;
        }

        public String getNoHp() {
            return noHp;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getImgProfile() {
            return imgProfile;
        }
    }
}
