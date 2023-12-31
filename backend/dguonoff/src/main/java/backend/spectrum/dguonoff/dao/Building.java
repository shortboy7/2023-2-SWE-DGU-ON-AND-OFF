package backend.spectrum.dguonoff.dao;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Building")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Building {
    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(nullable = false)
    private int maxFloor;

    @OneToMany(mappedBy = "building")
    private final List<Facility> facilities = new ArrayList<>();
}
