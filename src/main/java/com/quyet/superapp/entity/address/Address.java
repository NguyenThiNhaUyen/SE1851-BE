package com.quyet.superapp.entity.address;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "Address",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name_street", "ward_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "name_street", columnDefinition = "NVARCHAR(50)", nullable = false)
    private String addressStreet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    @JsonIgnore
    private Ward ward;
}
