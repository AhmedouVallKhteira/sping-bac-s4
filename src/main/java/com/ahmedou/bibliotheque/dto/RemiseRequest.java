package com.ahmedou.bibliotheque.dto;

public class RemiseRequest {
        private Long idLivre;
        private double remisePourcentage;

        public RemiseRequest() {}

        public RemiseRequest(Long idLivre, double remisePourcentage) {
            this.idLivre = idLivre;
            this.remisePourcentage = remisePourcentage;
        }

        public Long getIdLivre() {
            return idLivre;
        }

        public void setIdLivre(Long idLivre) {
            this.idLivre = idLivre;
        }

        public double getRemisePourcentage() {
            return remisePourcentage;
        }

        public void setRemisePourcentage(double remisePourcentage) {
            this.remisePourcentage = remisePourcentage;
        }
    }