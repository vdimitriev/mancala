package mk.vedmak.mancala.repository

import mk.vedmak.mancala.entity.Pocket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PocketRepository : JpaRepository<Pocket, Long> {

}