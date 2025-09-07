package org.szylica.service.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.szylica.database.repository.LockerRepository;
import org.szylica.database.repository.ParcelMachineRepository;
import org.szylica.files.repository.UserRepositoryFile;
import org.szylica.model.ParcelMachine;
import org.szylica.model.User;
import org.szylica.model.locker.Locker;
import org.szylica.model.locker.enums.LockerSize;
import org.szylica.model.locker.enums.LockerStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ParcelServiceImplTest {

    @Mock
    private ParcelMachineRepository parcelMachineRepository;

    @Mock
    private LockerRepository lockerRepository;

    @Mock
    private UserRepositoryFile userRepositoryFile;

    @InjectMocks
    private ParcelServiceImpl parcelServiceImpl;

    @Test
    @DisplayName("Should find the nearest parcel machine with an available locker")
    void test1(){
        var user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .latitude(50.0)
                .longitude(20.0)
                .build();

        when(userRepositoryFile.findById(anyLong())).thenReturn(Optional.of(user));

        var parcelMachine = ParcelMachine.builder()
                .id(1L)
                .name("PM1")
                .address("Address 1")
                .latitude(50.1)
                .longitude(20.1)
                .build();

        when(parcelMachineRepository.getOneClosestParcelMachineWithLockerAvailable(
                anyDouble(),
                anyDouble(),
                anyDouble(),
                eq(LockerSize.SMALL)))
                .thenReturn(Optional.of(parcelMachine));

        var result = parcelServiceImpl.findNearestParcelMachine(1L, LockerSize.SMALL, 20.0);
        assertThat(result)
                .isPresent()
                .contains(parcelMachine);

        verify(userRepositoryFile).findById(anyLong());
        verify(parcelMachineRepository).getOneClosestParcelMachineWithLockerAvailable(
                anyDouble(),
                anyDouble(),
                anyDouble(),
                eq(LockerSize.SMALL)
        );
    }

    @Test
    @DisplayName("Should throw exception when user is not found")
    void test2(){

        when(userRepositoryFile.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parcelServiceImpl.findNearestParcelMachine(1L, LockerSize.SMALL, 10.0))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("User not found");

        verify(userRepositoryFile).findById(anyLong());
        verifyNoInteractions(parcelMachineRepository, lockerRepository);
    }

    @Test
    @DisplayName("Should throw exception when no available locker is found")
    void test3(){
        when(lockerRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parcelServiceImpl.assignLockerToParcel(1L, LockerSize.SMALL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No free lockers in parcel machine with this size");

        verify(lockerRepository).getAllFreeLockersInSizeFromParcelMachine(eq(LockerSize.SMALL), anyLong());
        verify(lockerRepository, never()).updateLockerStatus(anyLong(), any(LockerStatus.class));

    }

    @Test
    @DisplayName("Should assign locker to parcel and update status when locker is available")
    void test4(){

        var locker = Locker.builder()
                .parcelMachineId(1L)
                .status(LockerStatus.FREE)
                .size(LockerSize.SMALL)
                .id(1L)
                .build();

        when(lockerRepository.getAllFreeLockersInSizeFromParcelMachine(eq(LockerSize.SMALL), anyLong()))
                .thenReturn(List.of(locker));
        when(lockerRepository.updateLockerStatus(anyLong(), eq(LockerStatus.OCCUPIED)))
                .thenReturn(true);

        var lockerId = parcelServiceImpl.assignLockerToParcel(1L, LockerSize.SMALL);

        assertThat(lockerId).isEqualTo(1L);

        verify(lockerRepository).getAllFreeLockersInSizeFromParcelMachine(eq(LockerSize.SMALL), anyLong());
        verify(lockerRepository).updateLockerStatus(1L, LockerStatus.OCCUPIED);
    }

    @Test
    @DisplayName("Should throw exception when locker status update fails")
    void test5(){

        var locker = Locker.builder()
                .parcelMachineId(1L)
                .status(LockerStatus.FREE)
                .size(LockerSize.SMALL)
                .id(1L)
                .build();

        when(lockerRepository.getAllFreeLockersInSizeFromParcelMachine(eq(LockerSize.SMALL), anyLong()))
                .thenReturn(List.of(locker));
        when(lockerRepository.updateLockerStatus(anyLong(), eq(LockerStatus.OCCUPIED)))
                .thenReturn(false);

        assertThatThrownBy(() -> parcelServiceImpl.assignLockerToParcel(1L, LockerSize.SMALL))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Failed to update locker status");

        verify(lockerRepository).getAllFreeLockersInSizeFromParcelMachine(eq(LockerSize.SMALL), anyLong());
        verify(lockerRepository).updateLockerStatus(1L, LockerStatus.OCCUPIED);
    }

}
