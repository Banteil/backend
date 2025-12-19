package com.example.club.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = -840415362L;

    public static final QMember member = new QMember("member1");

    public final StringPath email = createString("email");

    public final BooleanPath fromSocial = createBoolean("fromSocial");

    public final StringPath name = createString("name");

    public final StringPath password = createString("password");

    public final SetPath<com.example.club.entity.constant.ClubMemberRole, EnumPath<com.example.club.entity.constant.ClubMemberRole>> roles = this.<com.example.club.entity.constant.ClubMemberRole, EnumPath<com.example.club.entity.constant.ClubMemberRole>>createSet("roles", com.example.club.entity.constant.ClubMemberRole.class, EnumPath.class, PathInits.DIRECT2);

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}

