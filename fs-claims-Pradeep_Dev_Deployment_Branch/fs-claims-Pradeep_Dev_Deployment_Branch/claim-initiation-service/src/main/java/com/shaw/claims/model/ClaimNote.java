package com.shaw.claims.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.shaw.claims.serialization.LookupSerializer;
import com.shaw.claims.serialization.NoteGroupSerializer;
import com.shaw.claims.serialization.NoteTemplateSerializer;
import com.shaw.claims.serialization.NoteTypeSerializer;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Formula;

@Entity
@Table(name = "claimnote", schema = "clm")
@Data
public class ClaimNote extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claimnoteid")
    private int claimNoteId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notetypeid")
    @JsonSerialize(using = NoteTypeSerializer.class)
    private NoteType noteType;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = NoteGroupSerializer.class)
    @JoinColumn(name = "notegroupid")
    private NoteGroup noteGroup;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = NoteTemplateSerializer.class)
    @JoinColumn(name = "notetemplateid")
    private NoteTemplate noteTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claimid")
    @JsonIgnore
    private Claim claim;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LookupSerializer.class)
    @JoinColumn(name = "privacyid")
    private Lookup lookup;

    @Column(name = "calllog")
    private boolean callLog;

    @Column(name = "claimnotetext")
    private String claimNoteText;

    @Formula("SUBSTRING(claimnotetext, 1, 150)")
    private String claimNoteTextSubStr;

    @Column(name = "statusid")
    private int statusId;

    @OneToOne(mappedBy = "claimNote", cascade = CascadeType.ALL)
    private TraceTask traceTask;
}

