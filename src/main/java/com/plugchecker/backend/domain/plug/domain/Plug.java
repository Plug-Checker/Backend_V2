package com.plugchecker.backend.domain.plug.domain;

import com.plugchecker.backend.domain.auth.domain.User;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "plug")
public class Plug {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Setter
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(nullable = true)
    private boolean electricity;

}
