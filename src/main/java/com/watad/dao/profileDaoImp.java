package com.watad.dao;

import com.watad.dto.ProfileDtlDto;
import com.watad.entity.Profile;
import com.watad.enumValues.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
public class profileDaoImp implements ProfileDao{

    private final EntityManager entityManager;

    public profileDaoImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Profile> findAll() {
        TypedQuery<Profile> query = entityManager.createQuery(
                "SELECT p FROM Profile p JOIN FETCH p.user", Profile.class
        );
        return query.getResultList();
    }

    @Override
    public void saveProfile(Profile profile) {

        if (profile !=null){
            entityManager.persist(profile);
        }
    }

    @Override
    public Profile getProfileById(int id) {
        try {
            return entityManager.find(Profile.class,id);
        } catch (NoResultException e) {
            throw new NoResultException("There Is no Profile Founded with Id "+id);
        }
    }

    @Override
    public void editprofile(Profile profile) {
        entityManager.merge(profile);
    }

    @Override
    public String getPrfoileImageName(int id) {
        String jpql = "SELECT p.profileImagePath FROM Profile p WHERE p.id = :id";
        return entityManager.createQuery(jpql, String.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public List<ProfileDtlDto> findByUserPhone(String phone , int churchId , int meetingId) {
        String  jpql = " select new com.watad.dto.ProfileDtlDto(p.id, p.firstName, p.lastName , p.meetings.id , p.church.id  , p.user.id , p.phone , p.profileImagePath)  from Profile " +
                        " p where p.meetings.id = :meetingId and p.church.id = :churchId and  p.phone like :phone ";
         List<ProfileDtlDto>  result     =
                                        entityManager.createQuery(jpql,ProfileDtlDto.class)
                                            .setParameter("meetingId",meetingId)
                                            .setParameter("churchId",meetingId)
                                                .setParameter("phone", "%" + phone + "%").getResultList();
         if(result.isEmpty()) return new ArrayList<>();
         return  result;
    }

    @Override
    public List<ProfileDtlDto> findByUserPhoneOrUserName(String keyword, int churchId, int meetingId) {
        String  jpql = " select new com.watad.dto.ProfileDtlDto(p.id, p.firstName, p.lastName , p.meetings.id , p.church.id  , p.user.id , p.phone , p.profileImagePath)  from Profile " +
                " p where p.meetings.id = :meetingId and p.church.id = :churchId and  p.phone like :keyword OR  LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :keyword, '%'))";
        List<ProfileDtlDto>  result     =
                entityManager.createQuery(jpql,ProfileDtlDto.class)
                        .setParameter("meetingId",meetingId)
                        .setParameter("churchId",meetingId)
                        .setMaxResults(5)
                        .setParameter("keyword", "%" + keyword + "%").getResultList();

        if(result.isEmpty()) return new ArrayList<>();
        return  result;
    }

    @Override
    public List<ProfileDtlDto> findAllByFilterPaginated(int profileId , String status , String gender ,
                                                  int pageNum , int pageSize){
        StringBuilder jpql = new StringBuilder("""
        Select new com.watad.dto.ProfileDtlDto(
            p.id, p.firstName, p.lastName, p.gender,
            p.serviceStage.description, p.phone, p.birthday,
            p.address, p.priest.name, u.userName , u.isEnabled,u.id )
             From Profile p JOIN p.user u
             where 1 = 1
    """);
        if (!gender.equalsIgnoreCase("All")) {
            jpql.append(" and p.gender = :gender");
        }
        if (!status.equalsIgnoreCase("All")) {
            jpql.append(" AND u.isEnabled = :status");
        }
        if(profileId != 0){
            jpql.append(" AND p.id = :profileId");
        }
        jpql.append(" Order by p.firstName");
        TypedQuery<ProfileDtlDto> query = entityManager.createQuery(jpql.toString(), ProfileDtlDto.class);
        if (!status.equalsIgnoreCase("All")) {
            boolean statusValue = Boolean.parseBoolean(status);
            query.setParameter("status", statusValue);
        }
        if (!gender.equalsIgnoreCase("All")) {
            Gender genderEnum = Gender.valueOf(gender);
            query.setParameter("gender", genderEnum);
        }

        if (profileId != 0) {
            query.setParameter("profileId", profileId);
        }
        query.setFirstResult((pageNum - 1) * pageSize);
        query.setMaxResults(pageSize);
        List<ProfileDtlDto> resultList = query.getResultList();
        for(ProfileDtlDto dto :resultList){
            System.out.println(" profile "+dto.getFirstName() +" is enabled "+dto.isEnabled());
        }
        return  resultList;
    }

    @Override
    public int getTotalPagesByFilter(String status, String gender, int pageSize , int profileId ) {
        StringBuilder jpql = new StringBuilder("""
                        Select COUNT(p.id)
                        From Profile p JOIN p.user u
                        where 1 = 1
                """);

        if (!gender.equalsIgnoreCase("All")) {
            jpql.append(" and p.gender = :gender");
        }
        if (!status.equalsIgnoreCase("All")) {
            jpql.append(" AND u.isEnabled = :status");
        }
        if(profileId != 0){
            jpql.append(" AND p.id = :profileId");
        }
        Query query = entityManager.createQuery(jpql.toString());

        if (!gender.equalsIgnoreCase("All")) {
            Gender genderEnum = Gender.valueOf(gender);
            query.setParameter("gender", genderEnum);
        }
        if (!status.equalsIgnoreCase("All")) {
            boolean statusValue = Boolean.parseBoolean(status);
            query.setParameter("status", statusValue);
        }
        if (profileId != 0) {
            query.setParameter("profileId", profileId);
        }
        Long totalCount = (Long) query.getSingleResult();
        return (int) Math.ceil((double) totalCount / pageSize);
    }

    @Override
    public List<ProfileDtlDto> findProfileByNameOrPhone(String keyword , int churchId , int meetingId) {
        String jpql = """
                            Select new com.watad.dto.ProfileDtlDto(
                            p.id, p.firstName, p.lastName , p.phone)
                            From Profile p
                            Where p.meetings.id = :meetingId
                            and p.church.id = :churchId
                            and ( CONCAT(p.firstName, ' ', p.lastName) LIKE :keyword
                            OR p.phone LIKE :keyword )
                    """;
        TypedQuery<ProfileDtlDto> query = entityManager.createQuery(jpql, ProfileDtlDto.class);
        query.setParameter("meetingId",meetingId);
        query.setParameter("churchId",churchId);
        query.setMaxResults(5);
        query.setParameter("keyword","%"+keyword+"%");
        query.setMaxResults(5);
        return  query.getResultList();
    }

}
