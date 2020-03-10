package pl.fairit.somedayiwill.movie.usersmovies;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pl.fairit.somedayiwill.movie.movieapi.MDBMovie;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static java.util.Objects.isNull;

@Mapper
public interface MovieMapper {
    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    MovieDto mapMovieToMovieDto(Movie movie);

    Movie mapMovieDtoToMovie(MovieDto movieDto);

//    @Mapping(source = "overview", target = "description")
//    @Mapping(source = "poster_path", target = "posterLink")
//    @Mapping(source = "genres", target = "genres", qualifiedByName = "genresArrayToGenresString")
//    MovieDto mapMDBMovieToMovieDto(MDBMovie mdbMovie);

//    @Named("genresArrayToGenresString")
//    static String genresArrayToGenresString(List<String> genres) {
//        if (isNull(genres)) return "";
//        var joiner = new StringJoiner(", ");
//        for (String genre : genres) {
//            joiner.add(genre);
//        }
//        return joiner.toString();
//    }

    default MovieDto mapMDBMovieToMovieDto(MDBMovie mdbMovie, Map<Integer, String> genresMap) {
        if (mdbMovie == null) {
            return null;
        }

        var joiner = new StringJoiner(", ");
        Arrays.stream(mdbMovie.getGenre_ids())
                .forEach(genreId -> joiner.add(genresMap.get(genreId)));

        MovieDto movieDto = new MovieDto();
        movieDto.setGenres(joiner.toString());
        movieDto.setDescription(mdbMovie.getOverview());
        movieDto.setPosterLink(mdbMovie.getPoster_path());
        movieDto.setTitle(mdbMovie.getTitle());

        return movieDto;
    }
}