package lt.viko.eif.imazolevska.jaxrs.model.tmdb;

/**
 * Helper for the dates returned by TMDB in the {@code yyyy-MM-dd} format.
 */
final class TmdbDates {

    private static final int YEAR_LENGTH = 4;

    private TmdbDates() {
    }

    /**
     * Returns the year part of a TMDB date.
     *
     * @param date date in the {@code yyyy-MM-dd} format, may be {@code null} or empty
     * @return year, or {@code 0} if the date is missing or invalid
     */
    static int yearOf(String date) {
        if (date == null || date.length() < YEAR_LENGTH) {
            return 0;
        }
        try {
            return Integer.parseInt(date.substring(0, YEAR_LENGTH));
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
