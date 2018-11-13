package ru.homelab.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id", length = 12)
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(length = 12)
    private String catid;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "point_id")
    private Point point;

    private String addr;
    private String img;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "site_id")
    private Site site;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Service> services;
}
