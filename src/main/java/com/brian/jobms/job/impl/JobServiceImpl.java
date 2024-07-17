package com.brian.jobms.job.impl;


import com.brian.jobms.job.Job;
import com.brian.jobms.job.JobRepository;
import com.brian.jobms.job.JobService;
import com.brian.jobms.job.dto.JobWithCompanyDTO;
import com.brian.jobms.job.external.Company;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobServiceImpl implements JobService {
    JobRepository jobRepository;

    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<JobWithCompanyDTO> findAll() {
        List<Job> jobs = jobRepository.findAll();
        List<JobWithCompanyDTO> jobWithCompanyDTOS = new ArrayList<>();


        return jobs.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private JobWithCompanyDTO convertToDTO(Job job) {

        RestTemplate restTemplate = new RestTemplate();
        JobWithCompanyDTO jobWithCompanyDTO = new JobWithCompanyDTO();
        jobWithCompanyDTO.setJob(job);
        Company company = restTemplate.getForObject("http://localhost:8082/companies/" + job.getCompanyId(), Company.class);
        jobWithCompanyDTO.setCompany(company);

        return jobWithCompanyDTO;

    }

    @Override
    public void createJob(Job job) {
        jobRepository.save(job);
    }

    @Override
    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElse(null);
    }

    @Override
    public boolean deleteJobById(Long id) {
        if (jobRepository.existsById(id)) {
            jobRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean updateJob(Long id, Job updateJob) {
        if (jobRepository.existsById(id)) {
            jobRepository.save(updateJob);
            return true;
        }
        return false;
    }
}
