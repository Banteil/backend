package com.example.student.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.student.dto.StudentDTO;
import com.example.student.entity.Student;

@Configuration //스프링 설정 파일
public class RootConfig {
    @Bean //객체 생성해서 스프링 컨테이너가 관리
    ModelMapper getMapper()
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true) //필드명 같은 경우 매핑
        .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
        .setMatchingStrategy(MatchingStrategies.LOOSE);

        // 🌟 1. ID 필드 맵핑 무시 🌟
        modelMapper.typeMap(StudentDTO.class, Student.class).addMappings(mapper -> {
            // StudentDTO의 getId() 값을 Student 엔티티의 setId()에 맵핑하지 않도록 명시적으로 제외
            mapper.skip(Student::setId);
        });

        // 🌟 2. 생성 시간 필드 맵핑 무시 🌟
        modelMapper.typeMap(StudentDTO.class, Student.class).addMappings(mapper -> {
            // StudentDTO의 getCreateDateTime() 값을 Student 엔티티의 setCreateDateTime()에 맵핑하지 않도록 제외
            mapper.skip(Student::setCreateDateTime); 
 
        });
        return modelMapper;
    }
}
